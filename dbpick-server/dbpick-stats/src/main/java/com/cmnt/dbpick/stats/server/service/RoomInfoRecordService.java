package com.cmnt.dbpick.stats.server.service;

import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.RoomPlayRecordParams;
import com.cmnt.dbpick.stats.api.params.QueryRoomStatsParams;
import com.cmnt.dbpick.stats.api.vo.LiveRoomStatsVO;

import java.util.List;

/**
 * 存直播间统计信息
 */
public interface RoomInfoRecordService {

    /**
     * 统计房间流量明细信息
     */
    Boolean statsRoomPlayInfoDetail(RoomPlayRecordParams param);

    /**
     * 统计房间 trtc 通话分钟数据
     */
    Boolean statsRoomTRTCUsageInfo(RoomPlayRecordParams param);


    /**
     * 直播间流量明细数据
     */
    PageResponse<LiveRoomStatsVO> roomFluxDetail(QueryRoomStatsParams param);
    //查询最近三分钟直播数据
    List<LiveRoomStatsVO> threeSecondRoomRecord(RoomNoParam param);

}
