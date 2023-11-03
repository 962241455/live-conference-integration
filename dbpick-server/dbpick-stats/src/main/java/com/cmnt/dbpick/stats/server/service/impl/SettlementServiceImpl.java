package com.cmnt.dbpick.stats.server.service.impl;

import com.cmnt.dbpick.common.enums.live.MediaQualityEnum;
import com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum;
import com.cmnt.dbpick.common.tx.tencent.response.vod.TxDailyVodFilePlayVO;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.stats.api.vo.SettlementFluxVO;
import com.cmnt.dbpick.stats.api.vo.SettlementPlaybackVO;
import com.cmnt.dbpick.stats.api.vo.SettlementTransVO;
import com.cmnt.dbpick.stats.server.es.document.RoomFluxStatsIndex;
import com.cmnt.dbpick.stats.server.es.repository.RoomFluxStatsEsRepository;
import com.cmnt.dbpick.stats.server.mongodb.document.LiveVideos;
import com.cmnt.dbpick.stats.server.mongodb.repository.LiveVideosRepository;
import com.cmnt.dbpick.stats.server.service.PlaybackVodFluxService;
import com.cmnt.dbpick.stats.server.service.SettlementService;
import com.cmnt.dbpick.stats.server.utils.StreamingRoomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class SettlementServiceImpl implements SettlementService {

    @Autowired
    private StreamingRoomUtil streamingRoomUtil;
    @Autowired
    private LiveVideosRepository liveVideosRepository;

    @Autowired
    private RoomFluxStatsEsRepository roomFluxStatsEsRepository;

    @Autowired
    private PlaybackVodFluxService playbackVodFluxServiceImpl;

    /**
     * 查询商户指定时间内的播放流量
     */
    @Override
    public SettlementFluxVO thirdPlayFlux(ThirdRoomQueryParam param) {
        log.info("查询商户指定时间内的播放流量 thirdPlayFlux param={}", param);
        String startTime = param.getSearchStartTime();
        startTime = StringUtils.isBlank(startTime)?DateUtil.nowDateTime(DateUtil.Y_M_D):startTime;
        String endTime = param.getSearchEndTime();
        endTime = StringUtils.isBlank(endTime)?DateUtil.nowDateTime(DateUtil.Y_M_D):endTime;

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery("recordTime").from(startTime).to(endTime));
        query.must(QueryBuilders.termQuery("thirdId",param.getThirdId()));
        Iterable<RoomFluxStatsIndex> search = roomFluxStatsEsRepository.search(query);
        log.info("查询商户指定播放时间内的直播间 result={}", search);

        SettlementFluxVO vo = new SettlementFluxVO();
        AtomicReference<Long> sumFlux = new AtomicReference<>(0L);
        Map<String,SettlementFluxVO.DetailVO> cacheMap = new HashMap<>();
        search.forEach(data -> {
            handleFluxDetailVO(data, cacheMap, sumFlux);
        });
        System.err.println("---sumFlux-all--"+sumFlux);
        vo.setFluxRoomList(new ArrayList(cacheMap.values()));
        vo.setSumFlux(sumFlux.get());
        log.info("查询商户指定时间内的播放流量 return vo ={}", vo);
        return vo;
    }
    private void handleFluxDetailVO(RoomFluxStatsIndex data,
                                    Map<String,SettlementFluxVO.DetailVO> cacheMap, AtomicReference<Long> sumFlux){
        Long dataFlux = new Double(Math.ceil(data.getTotalFlux())).longValue();
        String roomNo = data.getRoomNo();
        SettlementFluxVO.DetailVO cacheVO = cacheMap.get(roomNo);
        List<SettlementFluxVO.DetailVO.DetailRecordVO> recordList = new ArrayList<>();
        Long roomOldFluxGB = 0L;
        if(Objects.isNull(cacheVO)){
            cacheVO = new SettlementFluxVO.DetailVO();
            FastBeanUtils.copy(data,cacheVO);
            StreamingRoomVO roomInfo = streamingRoomUtil.getByRoomNoAndRefreshRedis(roomNo);
            if(Objects.nonNull(roomInfo)){
                cacheVO.setTitle(roomInfo.getTitle());
                cacheVO.setType(roomInfo.getType());
            }
            cacheVO.setTotalFlux(dataFlux);
        } else {
            Long cacheOldFlux = cacheVO.getTotalFlux();
            roomOldFluxGB = new Double(Math.ceil(new Double(cacheOldFlux)/1000)).longValue();
            cacheVO.setTotalFlux(cacheOldFlux+dataFlux);
            recordList = cacheVO.getRecordList();
        }
        Long cacheNowFlux = cacheVO.getTotalFlux();
        Long roomNowFlux = new Double(Math.ceil(new Double(cacheNowFlux)/1000)).longValue();

        recordList.add(new SettlementFluxVO.DetailVO.DetailRecordVO(data.getRecordTime(),dataFlux));
        cacheVO.setRecordList(recordList);
        cacheVO.setTotalFluxGB(roomNowFlux);
        cacheMap.put(roomNo,cacheVO);
        Long nowAddFlux = roomNowFlux - roomOldFluxGB;
        sumFlux.updateAndGet(v -> v + nowAddFlux);

    }


    /**
     * 查询商户指定时间内的视频转码信息
     */
    @Override
    public SettlementTransVO thirdTransVideo(ThirdRoomQueryParam param) {
        log.info("查询商户指定时间内的视频转码信息 thirdTransVideo param={}", param);
        String nowDateYMS = DateUtil.nowDateTime(DateUtil.Y_M_D);
        String startTime = param.getSearchStartTime();
        startTime = StringUtils.isBlank(startTime)?nowDateYMS+" 00:00:00":startTime+" 00:00:00";
        String endTime = param.getSearchEndTime();
        endTime = StringUtils.isBlank(endTime)?nowDateYMS+" 23:59:59":endTime+" 23:59:59";
        List<LiveVideos> videos = liveVideosRepository.findByThirdIdAndCreateDateTimeBetweenOrderByCreateDateTimeDesc(
                param.getThirdId(), DateUtil.parseYMDHMS2Mils(startTime), DateUtil.parseYMDHMS2Mils(endTime)
        );
        log.info("查询商户指定时间内的视频转码信息 videos={}", videos);
        SettlementTransVO vo = new SettlementTransVO();
        List<SettlementTransVO.DetailVO> transVideoList = new ArrayList<>();
        AtomicReference<Long> transDuration = new AtomicReference<>(0L);
        videos.forEach(data -> {
            SettlementTransVO.DetailVO videoVO = new SettlementTransVO.DetailVO();
            FastBeanUtils.copy(data,videoVO);
            MediaQualityEnum quality = MediaQualityEnum.getMediaQualityByWH(data.getWidth(), data.getHeight());
            videoVO.setFileQuality(quality.getValue());
            videoVO.setCostSeconds(0L);
            if(StringUtils.equals(VideoTranscodeStatusEnum.FINISH.getValue(),data.getTranscodeStatus())){
                Long mins = new Double(Math.ceil(new Double(data.getSeconds()) / 60)).longValue();//分钟
                Long costMins = new Double(Math.ceil(quality.getRatio() * mins)).longValue();
                videoVO.setCostSeconds(costMins);
                transDuration.updateAndGet(v -> v + costMins);
            }
            transVideoList.add(videoVO);

        });
        vo.setTransVideoList(transVideoList);
        //vo.setTransDuration(new Double(Math.ceil(new Double(transDuration.get())/60)).longValue());
        vo.setTransDuration(transDuration.get());
        log.info("查询商户指定时间内的视频转码信息 return vo ={}", vo);
        return vo;
    }


    /**
     * 查询商户指定时间内的回放流量
     */
    @Override
    public SettlementPlaybackVO thirdPlaybackFlux(ThirdRoomQueryParam param) {
        log.info("查询商户指定时间内的回放流量 thirdPlaybackFlux param={}", param);
        SettlementPlaybackVO vo = new SettlementPlaybackVO();
        List<TxDailyVodFilePlayVO> voList = playbackVodFluxServiceImpl.vodVideoFlux(param);
        //Long sumFlux = voList.stream().mapToLong(TxDailyVodFilePlayVO::getFlux).sum();//字节
        //vo.setSumPlaybackFlux(new Double(Math.ceil(new Double(sumFlux)/1024/1024/1024)).intValue());
        Map<String,SettlementPlaybackVO.DetailVO> cacheMap = new HashMap<>();
        AtomicReference<Long> sumFlux = new AtomicReference<>(0L);
        //voList.stream().map(info-> handleFile(cacheMap,sumFlux,info)).collect(Collectors.toList());
        voList.forEach(info -> {
            handleFile(cacheMap,sumFlux,info);
        });
        vo.setPlaybackFluxList(new ArrayList(cacheMap.values()));
        vo.setSumPlaybackFlux(sumFlux.get());
        log.info("查询商户指定时间内的回放流量 return vo={}", vo);
        return vo;
    }
    //根据视频id对流量和次数汇总
    private void handleFile(Map<String,SettlementPlaybackVO.DetailVO> cacheMap, AtomicReference<Long> sumFlux,
                            TxDailyVodFilePlayVO info) {
        SettlementPlaybackVO.DetailVO cacheVO = cacheMap.get(info.getFileId());
        Long flux = new Double(Math.ceil(new Double(info.getFlux()) / 1024 / 1024)).longValue();

        Long fileOldFluxGB = 0L;
        if(Objects.isNull(cacheVO)){
            cacheVO = new SettlementPlaybackVO.DetailVO();
            FastBeanUtils.copy(info,cacheVO);
            cacheVO.setPlayFlux(flux);
        } else {
            cacheVO.setPlayTimes(cacheVO.getPlayTimes()+info.getPlayTimes());
            Long cacheOldFlux = cacheVO.getPlayFlux();
            fileOldFluxGB = new Double(Math.ceil(new Double(cacheOldFlux)/1024)).longValue();
            cacheVO.setPlayFlux(cacheOldFlux+flux);
        }
        Long fileNowFlux = new Double(Math.ceil(new Double(cacheVO.getPlayFlux()) / 1024)).longValue();
        cacheVO.setPlayFluxGB(fileNowFlux);
        cacheMap.put(info.getFileId(),cacheVO);

        Long nowAddFlux = fileNowFlux - fileOldFluxGB;
        sumFlux.updateAndGet(v -> v + nowAddFlux);
    }


}
