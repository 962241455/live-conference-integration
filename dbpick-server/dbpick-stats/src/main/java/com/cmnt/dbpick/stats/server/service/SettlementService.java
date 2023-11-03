package com.cmnt.dbpick.stats.server.service;

import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.stats.api.vo.SettlementFluxVO;
import com.cmnt.dbpick.stats.api.vo.SettlementPlaybackVO;
import com.cmnt.dbpick.stats.api.vo.SettlementTransVO;


/**
 * 直播结算数据相关接口
 */
public interface SettlementService {

    /**
     * 查询商户指定时间内的播放流量
     */
    SettlementFluxVO thirdPlayFlux(ThirdRoomQueryParam param);

    /**
     * 查询商户指定时间内的视频转码信息
     */
    SettlementTransVO thirdTransVideo(ThirdRoomQueryParam param);

    /**
     * 查询商户指定时间内的回放流量
     */
    SettlementPlaybackVO thirdPlaybackFlux(ThirdRoomQueryParam param);
}
