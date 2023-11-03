package com.cmnt.dbpick.stats.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.enums.live.MediaQualityEnum;
import com.cmnt.dbpick.common.tx.tencent.TxCloudVodUtil;
import com.cmnt.dbpick.common.tx.tencent.response.vod.TxDailyVodFilePlayVO;
import com.cmnt.dbpick.common.utils.ConstantUtil;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.RedisUtils;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.stats.api.params.ThirdVodPlaybackQueryParam;
import com.cmnt.dbpick.stats.api.vo.ThirdVodPlaybackVO;
import com.cmnt.dbpick.stats.server.es.ESUtils;
import com.cmnt.dbpick.stats.server.es.EsConstant;
import com.cmnt.dbpick.stats.server.mongodb.document.LiveVideos;
import com.cmnt.dbpick.stats.server.mongodb.repository.LiveVideosRepository;
import com.cmnt.dbpick.stats.server.service.PlaybackVodFluxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlaybackVodFluxServiceImpl implements PlaybackVodFluxService {

    @Autowired
    private ESUtils esUtils;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TxCloudVodUtil txCloudVodUtil;

    @Autowired
    private LiveVideosRepository liveVideosRepository;

    /**
     * 保存视频回放流量情况
     */
    @Override
    public Boolean savePlaybackVideoFlux() {
        String dateStr = DateUtil.now().minusDays(1).toString();
        log.info("保存视频回放流量情况, 查询日期：dateStr={}", dateStr);
        List<TxDailyVodFilePlayVO> allList = new ArrayList<>();
        String url = txCloudVodUtil.dailyVodFilePlayListZipUrl(dateStr);
        if(StringUtils.isNotBlank(url)){
            allList.addAll(txCloudVodUtil.analysisDailyVodFilePlayInfo(url));
        }
        Map<String, LiveVideos> thirdVideoCacheMap = new HashMap<>();
        List<String> batchId = allList.stream().map(vo ->(vo.getFileId()+"_"+vo.getDate())).collect(Collectors.toList());
        List<String> batchRecordJson = allList.stream().map(
                vo->findThirdIdByFileId(thirdVideoCacheMap,vo)).collect(Collectors.toList());

        log.info("保存视频回放流量情况, 查询日期：dateStr={}, 保存es数据={}", dateStr, allList.size());
        esUtils.putBatch(EsConstant.ES_INDEX_FILE_PLAYBACK_STATS, batchRecordJson, batchId);
        return Boolean.TRUE;
    }
    /** 查询使用所属商户id*/
    private String findThirdIdByFileId(Map<String, LiveVideos> thirdVideoCacheMap,TxDailyVodFilePlayVO vo) {
        String fileId = vo.getFileId();
        LiveVideos video = thirdVideoCacheMap.get(fileId);
        if(Objects.isNull(video)){
            video = liveVideosRepository.findTop1ByFileIdOrderByCreateDateTimeDesc(vo.getFileId());
            thirdVideoCacheMap.put(fileId, video);
        }
        if(Objects.nonNull(video)){
            vo.setThirdId(video.getThirdId());
            vo.setFileName(video.getFileName());
            vo.setDuration(video.getDuration());
            vo.setWidth(video.getWidth());
            vo.setHeight(video.getHeight());
            vo.setFileQuality(MediaQualityEnum.getMediaQualityByWH(video.getWidth(), video.getHeight()).getValue());
        }
        return JSON.toJSONString(vo);
    }


    /**
     * 初始化所有回放视频流量情况
     */
    @Async
    @Override
    public void initPlaybackVideoFlux(String startTime, String endTime) {
        log.info("初始化所有回放视频流量情况, startTime={} ~ endTime={}",startTime,endTime);
        if(StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)){
            return;
        }
        List<TxDailyVodFilePlayVO> allList = new ArrayList<>();
        Map<String, LiveVideos> thirdVideoCacheMap = new HashMap<>();
        List<String> dataStrList = ConstantUtil.findDaysStr(startTime, endTime);
        dataStrList.forEach(
                dateStr->{
                    String url = txCloudVodUtil.dailyVodFilePlayListZipUrl(dateStr);
                    allList.addAll(txCloudVodUtil.analysisDailyVodFilePlayInfo(url));
                }
        );
        List<String> batchId = allList.stream().map(vo -> (vo.getFileId()+"_"+vo.getDate())).collect(Collectors.toList());
        List<String> batchRecordJson = allList.stream().map(
                vo-> findThirdIdByFileId(thirdVideoCacheMap,vo)).collect(Collectors.toList());
        log.info("初始化所有回放视频流量情况, 保存es数据={}",batchId.size());
        esUtils.putBatch(EsConstant.ES_INDEX_FILE_PLAYBACK_STATS, batchRecordJson, batchId);
    }



    /**
     * 查询商户指定时间内的视频回放流量信息
     */
    @Override
    public List<TxDailyVodFilePlayVO> vodVideoFlux(ThirdRoomQueryParam param) {
        log.info("查询商户指定时间内的视频回放流量信息 vodVideoFlux param={}", param);
        String nowDateYMS = DateUtil.nowDateTime(DateUtil.Y_M_D);
        String startTime = StringUtils.isBlank(param.getSearchStartTime()) ?
                nowDateYMS : param.getSearchStartTime();
        String endTime = StringUtils.isBlank(param.getSearchEndTime()) ?
                nowDateYMS : param.getSearchEndTime();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termQuery("thirdId",param.getThirdId()));
        query.must(QueryBuilders.rangeQuery("date").from(startTime).to(endTime));
        List<TxDailyVodFilePlayVO> voList = new ArrayList<>();
        SearchResponse result = esUtils.searchAllByPage(EsConstant.ES_INDEX_FILE_PLAYBACK_STATS,
                query,0, 1000);
        if(Objects.nonNull(result)){
            SearchHits hits = result.getHits();
            hits.forEach(data -> voList.add(JSON.parseObject(data.getSourceAsString(), TxDailyVodFilePlayVO.class)));
        }
        log.info("查询商户指定时间内的视频回放流量信息 voList={}", voList);
        return voList;
    }


    /**
     * 查询vod视频回放详情列表
     */
    @Override
    public List<ThirdVodPlaybackVO> getVodFilePlaybackFlux(ThirdVodPlaybackQueryParam param) {
        log.info("查询商户指定视频回放流量信息 param={}", param);
        String thirdId = param.getThirdId();
        String fileId = param.getFileId();
        String redisKey = String.format(RedisKey.SETTLEMENT_PLAYBACK_FLUX, thirdId, fileId);

        Boolean searchByTime = Boolean.FALSE;
        String ctStartTm = param.getCountStartTm();
        String ctEndTm = param.getCountEndTm();
        if(StringUtils.equals(param.getSearchStatus(),"settlement")){
            searchByTime = Boolean.TRUE;
            redisKey = redisKey+"_"+ctStartTm+"_"+ctEndTm;
        }
        Set voSet = redisUtils.getSet(redisKey);
        if(Objects.isNull(voSet) || voSet.isEmpty()){
            BoolQueryBuilder query = QueryBuilders.boolQuery();
            query.must(QueryBuilders.termQuery("thirdId",thirdId));
            query.must(QueryBuilders.termQuery("fileId",fileId));
            if(searchByTime){
                query.must(QueryBuilders.rangeQuery("date").from(ctStartTm).to(ctEndTm));
            }
            SearchResponse result = esUtils.searchAllByPageOrderDesc(
                    EsConstant.ES_INDEX_FILE_PLAYBACK_STATS,
                    query,"date", 0, 1000);
            log.info("查询商户指定视频回放流量信息 es vo ={}", voSet);
            if(Objects.nonNull(result)){
                SearchHits hits = result.getHits();
                Boolean finalSearchByTime = searchByTime;
                String finalRedisKey = redisKey;
                hits.forEach(data -> {
                    ThirdVodPlaybackVO thirdVodPlaybackVO = JSON.parseObject(data.getSourceAsString(), ThirdVodPlaybackVO.class);
                    thirdVodPlaybackVO.setFlux(new Double(Math.ceil(new Double(thirdVodPlaybackVO.getFlux())/1024/1024)).longValue());
                    thirdVodPlaybackVO.setInCountTime(finalSearchByTime ?"in_tm":handleCountTm(ctStartTm,ctEndTm,thirdVodPlaybackVO.getDate()));
                    voSet.add(thirdVodPlaybackVO);
                    redisUtils.addSet(finalRedisKey, thirdVodPlaybackVO);
                });
            }
            redisUtils.expire(redisKey,60*60L);//有效期1小时
            log.info("查询商户指定视频回放流量信息 es vo ={}", voSet);
        }else{
            log.info("查询商户指定视频回放流量信息 redis vo ={}", voSet);
        }
        List<ThirdVodPlaybackVO> voList = new ArrayList<ThirdVodPlaybackVO>(voSet);
        //log.info("查询商户指定视频回放流量信息 return voList={}", voList);
        return voList;
    }
    //判断数据是否在结算时间之内
    private String handleCountTm(String ctStartTm, String ctEndTm, String ctTime) {
        if(StringUtils.isBlank(ctStartTm)||StringUtils.isBlank(ctEndTm)
                ||StringUtils.isBlank(ctTime)) return "out_tm";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = df.parse(ctStartTm);
            Date endDate = df.parse(ctEndTm);
            Date ctDate = df.parse(ctTime);
            if(!(ctDate.after(endDate) || ctDate.before(startDate))){
                return "in_tm";
            }
            return "out_tm";
        } catch (Exception e){
            return "out_tm";
        }
    }


}
