//package com.cmnt.dbpick.live.server.service;
//
//import com.cmnt.dbpick.common.page.PageResponse;
//import com.cmnt.dbpick.common.model.RoomNoParam;
//import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
//import com.cmnt.dbpick.stats.api.vo.LiveRoomStatsVO;
//import com.cmnt.dbpick.stats.api.vo.LiveUserRecordVO;
//import com.cmnt.dbpick.live.server.mongodb.document.LiveRoomUserRecord;
//
///**
// * 直播统计
// */
//public interface LiveStatsService {
//
//    /**
//     * 处理直播时长数据
//     * @return
//     */
//    Boolean handleRoomSecondsStats(String roomNo, String roomStatus);
//
//    /**
//     * 处理直播用户数据统计
//     * @return
//     */
//    Boolean handleRoomUserStats(LiveRoomUserRecord record);
//
//    /**
//     * 处理直播用户签到数据
//     * @return
//     */
//    Boolean handleRoomUserSign(LiveRoomUserRecord record);
//
//    /**
//     * 查询用户观看记录列表
//     * @param param
//     * @return
//     */
//    PageResponse<LiveUserRecordVO> userRecord(StreamingRoomQueryParams param);
//
//    /**
//     * 查询直播间统计数据
//     */
//    LiveRoomStatsVO findRoomStats(RoomNoParam param);
//}
