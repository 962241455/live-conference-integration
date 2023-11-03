//package com.cmnt.dbpick.live.server.service.impl;
//
//import com.cmnt.dbpick.common.enums.live.RoomStatusEnum;
//import com.cmnt.dbpick.common.enums.tencent.TxTerminalType;
//import com.cmnt.dbpick.common.enums.tencent.TxUserActType;
//import com.cmnt.dbpick.common.page.MongoPageHelper;
//import com.cmnt.dbpick.common.page.PageResponse;
//import com.cmnt.dbpick.common.utils.DateUtil;
//import com.cmnt.dbpick.common.utils.FastBeanUtils;
//import com.cmnt.dbpick.common.utils.JacksonUtils;
//import com.cmnt.dbpick.common.utils.ObjectTools;
//import com.cmnt.dbpick.common.model.RoomNoParam;
//import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
//import com.cmnt.dbpick.stats.api.vo.LiveRoomStatsVO;
//import com.cmnt.dbpick.stats.api.vo.LiveUserRecordVO;
//import com.cmnt.dbpick.live.server.mongodb.document.LiveRoomStats;
//import com.cmnt.dbpick.live.server.mongodb.document.LiveRoomUserRecord;
//import com.cmnt.dbpick.live.server.mongodb.document.LiveRoomUserStats;
//import com.cmnt.dbpick.live.server.mongodb.repository.LiveRoomStatsRepository;
//import com.cmnt.dbpick.live.server.mongodb.repository.LiveRoomUserStatsRepository;
//import com.cmnt.dbpick.live.server.service.LiveStatsService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.Objects;
//
///**
// * 回调事件
// */
//@Slf4j
//@Service
//public class LiveStatsServiceImpl implements LiveStatsService {
//
//    @Autowired
//    private MongoPageHelper mongoPageHelper;
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private LiveRoomStatsRepository liveRoomStatsRepository;
//    @Autowired
//    private LiveRoomUserStatsRepository liveRoomUserStatsRepository;
//    /**
//     * 处理直播时长数据
//     * @param roomNo
//     * @param roomStatus
//     * @return
//     */
//    @Override
//    public Boolean handleRoomSecondsStats(String roomNo, String roomStatus) {
//        log.info("直播间状态更改，更新直播时长, roomNo={}, status={}", roomNo, roomStatus);
//        RoomStatusEnum lookup = RoomStatusEnum.lookup(roomStatus);
//        LiveRoomStats roomStats = findRoomStatsByRoomNo(roomNo);
//        LocalDateTime localDateTime = ObjectTools.GetCurrentTime();
//        switch (lookup){
//            case LIVE_ING:
//                roomStats.setLastModifiedDate(localDateTime);
//                log.info("直播开始,  更新 LastModifiedDate = {} ",localDateTime);
//                liveRoomStatsRepository.save(roomStats);
//                break;
//            case LIVE_PAUSE :
//            case LIVE_OVER:
//                Long addSeconds = DateUtil.secondsBetween(roomStats.getLastModifiedDate(), localDateTime);
//                log.info("直播{}暂停/结束, 原直播时长:{}s, 添加本次直播时间:{}s",
//                        roomNo, roomStats.getLiveStartSeconds(), addSeconds);
//                mongoTemplate.findAndModify(
//                        new Query(Criteria.where("_id").is(roomStats.getId())),
//                        new Update().inc("liveStartSeconds", addSeconds).set("lastModifiedDate",localDateTime),
//                        LiveRoomStats.class);
//                break;
//            default:
//                log.info("没有操作 ：{} ",lookup.getDesc());
//                break;
//        }
//        return Boolean.TRUE;
//    }
//
//    /**
//     * 查询直播间数据
//     */
//    private LiveRoomStats findRoomStatsByRoomNo(String roomNo) {
//        LiveRoomStats roomStats = liveRoomStatsRepository.findTop1ByRoomNoOrderByCreateDateTimeDesc(roomNo);
//        if(Objects.isNull(roomStats)){
//            roomStats = LiveRoomStats.builder().roomNo(roomNo)
//                    .liveStartTime(DateUtil.fromDate2Str(new Date())).liveStartSeconds(0L)
//                    .pcWatchTimes(0L).mobileWatchTimes(0L).pcWatchSeconds(0L).mobileWatchSeconds(0L)
//                    .pcWatchPeople(0L).mobileWatchPeople(0L).pcStreamNum(0L).mobileStreamNum(0L)
//                    .build();
//            roomStats.initSave("");
//            roomStats = liveRoomStatsRepository.insert(roomStats);
//        }
//        return roomStats;
//    }
//
//
//    /**
//     * 处理直播用户数据统计
//     * @return
//     */
//    @Override
//    public Boolean handleRoomUserStats(LiveRoomUserRecord record) {
//        log.info("处理直播用户数据统计, record={}", record);
//        TxUserActType lookup = TxUserActType.lookup(record.getActType());
//        String roomNo = record.getRoomNo();
//        String userId = record.getUserId();
//        String terminalType = record.getTerminalType();
//        TxTerminalType terminalEnum = TxTerminalType.lookup(terminalType);
//        LiveRoomUserStats roomUserStats = findRoomUserStats(roomNo,userId,terminalType);
//        LiveRoomStats roomStats = findRoomStatsByRoomNo(roomNo);
//        log.info("用户:{} {} 直播间: {}.......", userId, lookup.getValue(), roomNo);
//        Update update = new Update();
//        switch (lookup){
//            case ENTER_ROOM:
//                log.info("统计用户数据： 观看次数+1");
//                mongoTemplate.findAndModify(
//                        new Query(Criteria.where("_id").is(roomUserStats.getId())),
//                        new Update().inc("watchTimes", 1).set("lastModifiedDate",ObjectTools.GetCurrentTime()),
//                        LiveRoomUserStats.class);
//
//                log.info("统计直播间数据：观看次数+1");
//                update.set("lastModifiedDate",ObjectTools.GetCurrentTime());
//                switch (terminalEnum){
//                    case ANDROID:
//                    case IOS:
//                        update.inc("mobileWatchTimes",1);
//                        if(Objects.equals(roomUserStats.getWatchTimes(),0L)){
//                            update.inc("mobileWatchPeople",1);
//                        }
//                        break;
//                    default:
//                        update.inc("pcWatchTimes",1);
//                        if(Objects.equals(roomUserStats.getWatchTimes(),0L)){
//                            update.inc("pcWatchPeople",1);
//                        }
//                        break;
//                }
//                mongoTemplate.findAndModify(
//                        new Query(Criteria.where("_id").is(roomStats.getId())), update, LiveRoomStats.class);
//                break;
//            case EXIT_ROOM:
//                LocalDateTime localDateTime = ObjectTools.GetCurrentTime();
//                Long addSeconds = DateUtil.secondsBetween(roomUserStats.getLastModifiedDate(), localDateTime);
//                log.info("统计用户数据：添加本次观看时长={}", addSeconds);
//                mongoTemplate.findAndModify(
//                        new Query(Criteria.where("_id").is(roomUserStats.getId())),
//                        new Update().inc("watchSeconds", addSeconds).set("lastModifiedDate",localDateTime),
//                        LiveRoomStats.class);
//
//                log.info("统计直播间数据：添加本次观看时长={}", addSeconds);
//                update.set("lastModifiedDate",localDateTime);
//                switch (terminalEnum){
//                    case ANDROID:
//                    case IOS:
//                        update.inc("mobileWatchSeconds",addSeconds);
//                        break;
//                    default:
//                        update.inc("pcWatchSeconds",addSeconds);
//                        break;
//                }
//                mongoTemplate.findAndModify(
//                        new Query(Criteria.where("_id").is(roomStats.getId())), update, LiveRoomStats.class);
//                break;
//            default:
//                log.info("没有操作 ：{} ",lookup.getValue());
//                break;
//        }
//        return Boolean.TRUE;
//    }
//
//    /**
//     * 处理直播用户签到数据
//     * @return
//     */
//    @Override
//    public Boolean handleRoomUserSign(LiveRoomUserRecord record) {
//        log.info("处理直播用户签到数据, record={}", record);
//        String roomNo = record.getRoomNo();
//        String userId = record.getUserId();
//        LiveRoomUserStats roomUserStats = findRoomUserStats(roomNo,userId,record.getTerminalType());
//        log.info("用户:{} 在直播间: {} 签到.......", userId, roomNo);
//        Update update = new Update();
//        update.inc("signTimes", 1);
//        String nowDateStr = DateUtil.fromDate2Str(new Date());
//        if(Objects.equals(roomUserStats.getWatchStartTime(),roomUserStats.getFirstSignTime())){
//            update.set("firstSignTime",nowDateStr);
//        }
//        update.set("lastSignTime",nowDateStr);
//        mongoTemplate.findAndModify(
//                new Query(Criteria.where("_id").is(roomUserStats.getId())), update, LiveRoomUserStats.class);
//        return Boolean.TRUE;
//    }
//
//    /**
//     * 查询直播间用户数据统计
//     */
//    private LiveRoomUserStats findRoomUserStats(String roomNo, String userId, String terminalType) {
//        LiveRoomUserStats roomUserStats = liveRoomUserStatsRepository.findTop1ByRoomNoAndUserIdOrderByCreateDateTimeDesc(
//                roomNo, userId);
//        if(Objects.isNull(roomUserStats)){
//            String nowDateStr = DateUtil.fromDate2Str(new Date());
//            roomUserStats = LiveRoomUserStats.builder().roomNo(roomNo).userId(userId)
//                    .watchStartTime(nowDateStr).watchSeconds(0L).watchTimes(0L)
//                    .firstSignTime(nowDateStr).lastSignTime(nowDateStr).signTimes(0L)
//                    .terminalType(StringUtils.isBlank(terminalType)?"/":terminalType)
//                    .ip("0:0:0:0:0:0:0:1").area("China").build();
//            roomUserStats.initSave("");
//            roomUserStats = liveRoomUserStatsRepository.insert(roomUserStats);
//        }
//        return roomUserStats;
//    }
//
//
//
//
//    /**
//     * 查询用户观看记录列表
//     * @param param
//     * @return
//     */
//    @Override
//    public PageResponse<LiveUserRecordVO> userRecord(StreamingRoomQueryParams param) {
//        PageResponse<LiveUserRecordVO> response = new PageResponse<>();
//        if(StringUtils.isBlank(param.getRoomNo())){
//            return response;
//        }
//        Query query = new Query();
//        query.addCriteria(Criteria.where("roomNo").regex(param.getRoomNo()));
//        PageResponse<LiveRoomUserRecord> objects = mongoPageHelper.pageQuery(query, LiveRoomUserRecord.class, param);
//        return JacksonUtils.toBean(JacksonUtils.toJson(objects), PageResponse.class);
//    }
//
//
//    /**
//     * 查询直播间统计数据
//     */
//    @Override
//    public LiveRoomStatsVO findRoomStats(RoomNoParam param){
//        LiveRoomStatsVO vo = new LiveRoomStatsVO();
//        if(StringUtils.isBlank(param.getRoomNo())){
//            return vo;
//        }
//        LiveRoomStats roomStats = liveRoomStatsRepository.findTop1ByRoomNoOrderByCreateDateTimeDesc(param.getRoomNo());
//        if(Objects.nonNull(roomStats)){
//            FastBeanUtils.copy(roomStats,vo);
//        }
//        return vo;
//    }
//
//}
