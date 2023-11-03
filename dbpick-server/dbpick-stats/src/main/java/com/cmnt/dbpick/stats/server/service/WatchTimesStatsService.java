package com.cmnt.dbpick.stats.server.service;


import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.stats.api.params.AnalyseWatchTimesParam;
import com.cmnt.dbpick.stats.api.params.QueryRoomStatsParams;
import com.cmnt.dbpick.stats.api.vo.es.RoomUserRecordNo;
import com.cmnt.dbpick.stats.api.vo.es.RoomUserRecordOnceNo;
import com.cmnt.dbpick.stats.api.vo.es.RoomUserRecordStatsNo;

import javax.servlet.http.HttpServletResponse;

/**
 * 直播间流量统计
 */
public interface WatchTimesStatsService {

    /**
     * es数据分析
     */
    void analyseWatchTimes(String roomNo, String ak);
    /** es数据分析-管理端 */
    Boolean analyseWatchTimes(AnalyseWatchTimesParam param);

    RoomUserRecordOnceNo initRoomUserRecordOnceNo(RoomUserRecordNo enterRecord,
                                                  RoomUserRecordNo exitRecord,
                                                  String watchType,
                                                  String watchTimeStr, Long watchTime);


    /**
     * 查询分析完成的数据
     */
    PageResponse<RoomUserRecordStatsNo> stats(StreamingRoomQueryParams param, String ak);
    /** 查询分析完成的数据-管理端 */
    PageResponse<RoomUserRecordStatsNo> stats(QueryRoomStatsParams param);

    /**
     * 导出观看时长
     */
    void exportStatsData(String roomNo, HttpServletResponse response);

    void setRedisKey(String redisKey);

}
