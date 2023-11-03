package com.cmnt.dbpick.stats.server.service;


import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.stats.api.vo.RoomFluxStatsVO;
import com.cmnt.dbpick.stats.api.vo.UserRoomFluxVO;

import java.util.List;


/**
 * 直播间流量统计
 */
public interface RoomFluxStatsService {

    /**
     * 保存直播间流量统计
     */
    Boolean saveRoomFluxStats();
    /**
     * 查询天维度查询 TRTC音视频用量。
     */
    Boolean saveAllTrtcUsage();

//    /**
//     * 查询多个历史房间流量信息
//     */
//    Boolean roomFluxStatsServiceImpl(String roomNos);


    /**
     * 查询直播间播放流量
     * @param param
     * @return
     */
    RoomFluxStatsVO roomPlayFlux(RoomNoParam param);

    /**
     * 查询商户指定播放时间内的每个直播间的播放流量
     */
    List<UserRoomFluxVO> userRoomPlayFlux(ThirdRoomQueryParam param);

}
