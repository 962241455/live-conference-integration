package com.cmnt.dbpick.stats.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.common.utils.StringUtils;
import com.cmnt.dbpick.stats.api.vo.es.RoomUserRecordNo;
import com.cmnt.dbpick.stats.api.vo.es.RoomUserRecordOnceNo;
import com.cmnt.dbpick.stats.api.vo.es.RoomUserRecordStatsNo;
import com.cmnt.dbpick.stats.api.vo.es.WatchTimesPageParam;
import com.cmnt.dbpick.stats.server.es.ESUtils;
import com.cmnt.dbpick.stats.server.event.WatchTypeEventType;
import com.cmnt.dbpick.stats.server.service.WatchTimesStatsService;
import com.cmnt.dbpick.stats.server.service.WatchTypeAnalyseService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分析直播观看数据
 */
@Slf4j
@Component
@WatchTypeEventType(WatchRoomTypeEnum.LIVE)
public class WatchTypeLiveServiceImpl implements WatchTypeAnalyseService {

    @Autowired
    private ESUtils esUtils;

    @Lazy
    @Autowired
    private WatchTimesStatsService watchTimesStatsServiceImpl;

    @Override
    public void executeAnalyse(WatchTimesPageParam analysePageParam, String roomNo) {
        log.info("es数据分析-分析直播观看数据 analysePageParam={}， roomNo={}", analysePageParam, roomNo);
        Integer pageSize = analysePageParam.getPageSize();
        List<String> statsJsons = new ArrayList<>();
        List<String> statsIds = new ArrayList<>();
        List<String> onceJsons = new ArrayList<>();
        List<String> onceIds = new ArrayList<>();
        try{
            String searchIndex = analysePageParam.getUserRecordIdx();
            log.info("es数据分析-分析直播观看数据 分析记录表={}", searchIndex);
            Map<String, RoomUserRecordNo> userRecordCacheMap = new HashMap();
            Map<String, RoomUserRecordStatsNo> statsCacheMap = new HashMap();
            for (int i = 0; i <= analysePageParam.getMaxPage(); i++) {
                Integer from = i*pageSize;
                SearchResponse result = esUtils.searchAllByPageOrderAsc(searchIndex,"actTimeMillis",
                        new HashMap<String,Object>(){{put("roomNo", roomNo);}},from,pageSize);
                SearchHits hits = result.getHits();
                hits.forEach(
                        data -> {
                            RoomUserRecordNo record = JSON.parseObject(data.getSourceAsString(), RoomUserRecordNo.class);
                            if(StringUtils.isNotBlank(record.getWatchType()) &&
                                    StringUtils.equals(record.getWatchType(), WatchRoomTypeEnum.PLAYBACK.getValue())){
                                return;
                            }
                            String key = record.getUserId();
                            switch (record.getActType()){
                                case "enter":
                                    if(!userRecordCacheMap.containsKey(key)) userRecordCacheMap.put(key,record);
                                    break;
                                case "exit":
                                    if(userRecordCacheMap.containsKey(key)) {
                                        // 个人单次观看时长
                                        RoomUserRecordNo enterRecord = userRecordCacheMap.get(key);
                                        Long watchTimes = record.getActTimeMillis()- enterRecord.getActTimeMillis();
                                        Long watchTimeCeil = DateUtil.ceilTimestamp(watchTimes);
                                        String watchTimeCeilStr = DateUtil.formatMilliSecondToTimeStr(watchTimeCeil);
                                        userRecordCacheMap.remove(key);

                                        RoomUserRecordOnceNo onceNo = watchTimesStatsServiceImpl.initRoomUserRecordOnceNo(
                                                enterRecord, record, WatchRoomTypeEnum.LIVE.getValue(), watchTimeCeilStr, watchTimeCeil);
                                        onceIds.add(onceNo.getId());
                                        onceJsons.add(JSON.toJSONString(onceNo));


                                        // 个人总观看时长
                                        RoomUserRecordStatsNo statsOne = new RoomUserRecordStatsNo();
                                        Long statsWatchTimeCeil = watchTimeCeil;
                                        if(!statsCacheMap.containsKey(key)){
                                            FastBeanUtils.copy(enterRecord,statsOne);
                                        } else {
                                            statsOne = statsCacheMap.get(key);
                                            statsWatchTimeCeil = statsOne.getWatchTotalTimes()+watchTimeCeil;
                                        }
                                        statsOne.setWatchTotalTimes(statsWatchTimeCeil);
                                        statsOne.setWatchTotalTimesStr(DateUtil.formatMilliSecondToTimeStr(statsWatchTimeCeil));
                                        statsOne.setThirdUserId(record.getThirdUserId());
                                        statsOne.setWatchType(WatchRoomTypeEnum.LIVE.getValue());
                                        statsCacheMap.put(key,statsOne);
                                        statsJsons.add(JSON.toJSONString(statsOne));
                                        statsIds.add(statsOne.getId());
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                );
                if(hits.getHits().length<pageSize){
                    break;
                }
            }
        } catch (Exception e){
            throw new BizException("索引不存在");
        }
        Map fieldParam = new HashMap<String,String>(){{
            put("roomNo",roomNo);
        }};
        String userStatsIdx = analysePageParam.getUserRecordStatsIdx();
        esUtils.deleteIndexByFieldParamRLike(userStatsIdx,fieldParam);
        log.info("保存直播观看数据分析结果 index={}， size={}", userStatsIdx, statsJsons.size());
        esUtils.putBatch(userStatsIdx, statsJsons, statsIds);

        String userOnceIdx = analysePageParam.getUserRecordOnceIdx();
        esUtils.deleteIndexByFieldParamRLike(userOnceIdx,fieldParam);
        log.info("保存直播观看数据分析结果 index={}， size={}", userOnceIdx, onceJsons.size());
        esUtils.putBatch(userOnceIdx, onceJsons, onceIds);
    }

}
