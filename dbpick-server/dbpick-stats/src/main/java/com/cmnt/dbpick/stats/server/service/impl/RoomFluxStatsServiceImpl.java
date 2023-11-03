package com.cmnt.dbpick.stats.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.enums.SortEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.tx.tencent.TxCloudLiveUtil;
import com.cmnt.dbpick.common.tx.tencent.TxCloudTrtcUtil;
import com.cmnt.dbpick.common.tx.tencent.request.live.TxStreamDayInfoParam;
import com.cmnt.dbpick.common.tx.tencent.request.trtc.TxDayUsageInfoParam;
import com.cmnt.dbpick.common.tx.tencent.response.live.eventInfo.TxStreamDayPlayInfo;
import com.cmnt.dbpick.common.tx.tencent.response.trtc.DayUsageResponse;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.stats.api.vo.RoomFluxStatsVO;
import com.cmnt.dbpick.stats.api.vo.UserRoomFluxVO;
import com.cmnt.dbpick.stats.server.es.ESUtils;
import com.cmnt.dbpick.stats.server.es.EsConstant;
import com.cmnt.dbpick.stats.server.es.document.LiveTrtcUsageIndex;
import com.cmnt.dbpick.stats.server.es.document.RoomFluxStatsIndex;
import com.cmnt.dbpick.stats.server.es.repository.LiveTrtcUsageEsRepository;
import com.cmnt.dbpick.stats.server.es.repository.RoomFluxStatsEsRepository;
import com.cmnt.dbpick.stats.server.service.RoomFluxStatsService;
import com.cmnt.dbpick.stats.server.utils.StreamingRoomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomFluxStatsServiceImpl implements RoomFluxStatsService {

    @Autowired
    private ESUtils esUtils;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TxCloudLiveUtil txCloudLiveUtil;
    @Autowired
    private TxCloudTrtcUtil txCloudTrtcUtil;

    @Autowired
    private StreamingRoomUtil streamingRoomUtil;

    @Autowired
    private RoomFluxStatsEsRepository roomFluxStatsEsRepository;
    @Autowired
    private LiveTrtcUsageEsRepository liveTrtcUsageEsRepository;


    @Override
    public Boolean saveRoomFluxStats() {
        String dateStr = DateUtil.now().minusDays(1).toString();
        log.info("查询天维度每条流的播放数据, 查询日期：dateStr={}", dateStr);
        TxStreamDayInfoParam param = TxStreamDayInfoParam.builder().DayTime(dateStr).build();
        List<TxStreamDayPlayInfo> streamDayPlayInfoList = txCloudLiveUtil.findAllStreamDayPlayInfo(param);
        List<RoomFluxStatsIndex> voList = new ArrayList<>();
        streamDayPlayInfoList.forEach(info->voList.add(initRoomFluxStats(info,dateStr)));
        roomFluxStatsEsRepository.saveAll(voList);
        log.info("查询天维度每条流的播放数据, 查询日期：dateStr={}, 保存es数据={}", dateStr,voList.size());
        return Boolean.TRUE;
    }

    private RoomFluxStatsIndex initRoomFluxStats(TxStreamDayPlayInfo info,String dateStr){
        String roomNo = info.getStreamName();
        RoomFluxStatsIndex roomInfoRecord = RoomFluxStatsIndex.builder().id(roomNo + "_" + dateStr)
                .roomNo(roomNo).totalFlux(info.getTotalFlux())
                .recordTime(dateStr).recordTimeMillis(DateUtil.getTimeStrampSeconds()).build();
        String thirdId = "";
        StreamingRoomVO roomVO = streamingRoomUtil.getByRoomNoAndRefreshRedis(roomNo);
        if (Objects.nonNull(roomVO)){
            thirdId = roomVO.getThirdId();
        }
        roomInfoRecord.setThirdId(thirdId);
        return roomInfoRecord;
    }

    /**
     * 查询每天 TRTC音视频用量。
     */
    @Override
    public Boolean saveAllTrtcUsage(){
        String dateStr = DateUtil.now().minusDays(1).toString();
        log.info("查询每天TRTC音视频用量, 查询日期：dateStr={}", dateStr);
        TxDayUsageInfoParam param = TxDayUsageInfoParam.builder()
                .startTime(DateUtil.now().minusDays(1).toString())
                .endTime(DateUtil.nowDateTime(DateUtil.Y_M_D)).build();

        List<DayUsageResponse.DayUsageDetail> allUsage = txCloudTrtcUtil.findAllDayUsageInfo(param);
        List<LiveTrtcUsageIndex> voList = new ArrayList<>();
        allUsage.forEach(info->voList.add(
                LiveTrtcUsageIndex.builder().id("trtc_" + info.getTimeKey())
                        .recordTime(info.getTimeKey())
                        .Audio(info.getUsageValue().get(0)).SD(info.getUsageValue().get(1))
                        .HD(info.getUsageValue().get(2)).FullHD(info.getUsageValue().get(3))
                        .video_2K(info.getUsageValue().get(4)).video_4K(info.getUsageValue().get(5))
                        .build()
        ));
        liveTrtcUsageEsRepository.saveAll(voList);
        log.info("查询每天TRTC音视频用量, 查询日期：dateStr={}, 保存es数据={}", dateStr,voList.size());
        return Boolean.TRUE;
    }

    /**
     * 查询直播间播放流量
     * @param param
     * @return
     */
    @Override
    public RoomFluxStatsVO roomPlayFlux(RoomNoParam param) {
        log.info("查询直播间播放流量roomPlayFlux: param={}",param);
        if (Objects.isNull(param) || StringUtils.isBlank(param.getRoomNo())){
            log.error("房间号 不能为空");
            throw new BizException("房间号 不能为空");
        }
        String indexName = EsConstant.ES_INDEX_ROOM_FLUX_STATS;
        SearchResponse result = esUtils.search10ByFieldOrder(indexName,
                "recordTimeMillis", SortEnum.DESC.getValue(),
                new HashMap<String,Object>(){{put("roomNo", param.getRoomNo());}} );
        SearchHits hits = result.getHits();
        RoomFluxStatsVO vo = new RoomFluxStatsVO();
        List<RoomFluxStatsVO.RoomFluxRecordVO> fluxRecordList = new ArrayList<>();
        AtomicReference<Double> sumFlux = new AtomicReference<>(0d);
        hits.forEach(data -> {
            RoomFluxStatsVO.RoomFluxRecordVO recordVO =
                    JSON.parseObject(data.getSourceAsString(), RoomFluxStatsVO.RoomFluxRecordVO.class);
            fluxRecordList.add(recordVO);
            sumFlux.updateAndGet(v -> v + recordVO.getTotalFlux());
        });
        vo.setFluxRecordList(fluxRecordList);
        vo.setRoomNo(param.getRoomNo());
        vo.setSumFlux(new Double(Math.ceil(sumFlux.get()/1000)).intValue());
        return vo;
    }

    /**
     * 查询商户指定播放时间内的每个直播间的播放流量
     */
    @Override
    public List<UserRoomFluxVO> userRoomPlayFlux(ThirdRoomQueryParam param){
        log.info("查询商户指定播放时间内的每个直播间的播放流量 userRoomPlayFlux param={}", param);
        List<String> roomNos = streamingRoomUtil.getRoomNosByThird(param);
        log.info("查询商户指定播放时间内的直播间 roomNos={}", roomNos);

        BoolQueryBuilder queryParam = QueryBuilders.boolQuery();
        queryParam.must(QueryBuilders.termsQuery("roomNo",roomNos));
        Iterable<RoomFluxStatsIndex> search = roomFluxStatsEsRepository.search(queryParam);
        log.info("查询商户指定播放时间内的直播间 result={}", search);
        Map<String,Float> roomFluxCacheMap = new HashMap<>();
        search.forEach(vo-> roomFluxCacheMap.put(vo.getRoomNo(),
                (roomFluxCacheMap.containsKey(vo.getRoomNo())?roomFluxCacheMap.get(vo.getRoomNo()):0F)+vo.getTotalFlux()));
        List<UserRoomFluxVO> voList = roomFluxCacheMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e -> new UserRoomFluxVO(e.getKey(), e.getValue())).collect(Collectors.toList());
        return voList;
    }


//    @Autowired
//    private RoomInfoRecordService roomInfoRecordServiceImpl;
//    /**
//     * 查询多个历史房间流量信息
//     */
//    @Override
//    public Boolean roomFluxStatsServiceImpl(String roomNos){
//        String[] roomNoSplit = roomNos.split(",");
//        Query query = new Query();
//        query.addCriteria(Criteria.where("roomNo").in(roomNoSplit));
//        PageResponse<StreamingRoom> objects = mongoPageHelper.pageQuery(
//                query, StreamingRoom.class, new StreamingRoomQueryParams());
//        Set<String> fluxDate = new HashSet();
//        log.info("保存全量流明细数据开始...");
//        objects.getData().forEach(
//                roomInfo -> {
//                    if(redisUtils.isSetMember(RedisKey.HIS_ROOM_FULL_FLUX_STATS, roomInfo.getRoomNo())){
//                        log.info("该房间已经统计过...");
//                        return;
//                    }
//                    redisUtils.addSet(RedisKey.HIS_ROOM_FULL_FLUX_STATS, roomInfo.getRoomNo());
//                    String playStartTime = DateUtil.addDateSecond(DateUtil.dateTime2Str(roomInfo.getCreateDate(), DateUtil.Y_M_D_HMS),-3*60);
//                    String playStopTime = DateUtil.addDateSecond(DateUtil.dateTime2Str(roomInfo.getLastModifiedDate(), DateUtil.Y_M_D_HMS),3*60);
//                    log.info("统计房间流量明细信息，根据直播时间前后冗余3分钟 search room={} day={}~{}",roomInfo.getRoomNo(),playStartTime,playStopTime);
//                    List<DateUtil.CurrentDay> dayList = DateUtil.getDataStrList(playStartTime, playStopTime);
//                    dayList.forEach(
//                            dayStr -> {
//                                fluxDate.add(DateUtil.date2YmdStr(DateUtil.str2Date(dayStr.getCurrentDayMin(), DateUtil.Y_M_D_HMS)));
//                                roomInfoRecordServiceImpl.statsRoomPlayInfoDetail(
//                                        RoomPlayRecordParams.builder().startTime(dayStr.getCurrentDayMin())
//                                                .stopTime(dayStr.getCurrentDayMax()).roomNo(roomInfo.getRoomNo()).build());
//                            }
//                    );
//                }
//        );
//        log.info("保存全量流明细数据完成...保存全量流统计数据开始...");
//        fluxDate.forEach(
//                dateStr->{
//                    log.info("查询天维度每条流的播放数据, 查询日期：dateStr={}", dateStr);
//                    List<TxStreamDayPlayInfo> streamDayPlayInfoList = txCloudLiveUtil.findAllStreamDayPlayInfo(
//                            TxStreamDayInfoParam.builder().DayTime(dateStr).build());
//                    List<RoomFluxStatsIndex> voList = new ArrayList<>();
//                    streamDayPlayInfoList.forEach(info->voList.add(initRoomFluxStats(info,dateStr)));
//                    roomFluxStatsEsRepository.saveAll(voList);
//                    log.info("查询天维度每条流的播放数据, 查询日期：dateStr={}, 保存es数据={}", dateStr,voList.size());
//                }
//        );
//        log.info("保存全量流统计数据完成...");
//        return Boolean.TRUE;
//    }


}
