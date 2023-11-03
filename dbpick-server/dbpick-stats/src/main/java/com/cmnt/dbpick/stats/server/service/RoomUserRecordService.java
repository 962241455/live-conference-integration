package com.cmnt.dbpick.stats.server.service;

import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.stats.api.params.QueryRoomStatsParams;
import com.cmnt.dbpick.stats.api.vo.LiveUserRecordVO;
import com.cmnt.dbpick.stats.api.params.RoomUserRecordParam;
import com.cmnt.dbpick.stats.api.vo.LiveUserStatsVO;
import com.cmnt.dbpick.stats.server.es.document.RoomUserRecordIndex;

import java.util.List;

/**
 * 存直播间用户记录
 */
public interface RoomUserRecordService {

    /**
     * 保存im直播间用户记录
     * @param param
     */
    List<RoomUserRecordIndex> saveRoomUserRecord(RoomUserRecordParam param);

    void handleRoomHotAndOnline(List<RoomUserRecordIndex> hotOnlineList);
    /**
     * 查询用户观看记录列表
     * @param param
     * @return
     */
    PageResponse<LiveUserRecordVO> userRecord(QueryRoomStatsParams param);

    /**
     *  异步保存不同数据到不同房间es--(会创建过多索引，所以废弃-20221206)
     */
    //Boolean saveRecordListByRoomNo(List<RoomUserRecordIndex> recordList);

    /**
     * 查询用户观看数据统计列表
     */
    PageResponse<LiveUserStatsVO> userStats(QueryRoomStatsParams param);

}
