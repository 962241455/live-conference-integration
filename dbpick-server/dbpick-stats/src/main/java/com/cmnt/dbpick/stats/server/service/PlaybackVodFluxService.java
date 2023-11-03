package com.cmnt.dbpick.stats.server.service;

import com.cmnt.dbpick.common.tx.tencent.response.vod.TxDailyVodFilePlayVO;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.stats.api.params.ThirdVodPlaybackQueryParam;
import com.cmnt.dbpick.stats.api.vo.ThirdVodPlaybackVO;

import java.util.List;

/**
 * 视频回放流量情况
 */
public interface PlaybackVodFluxService {

    /**
     * 保存视频回放流量情况
     */
    Boolean savePlaybackVideoFlux();

    /**
     * 初始化所有回放视频流量情况
     * @return
     */
    void initPlaybackVideoFlux(String startTime, String endTime);

    /**
     * 查询商户指定时间内的视频回放流量信息
     */
    List<TxDailyVodFilePlayVO> vodVideoFlux(ThirdRoomQueryParam param);

    /**
     * 查询vod视频回放详情列表
     */
    List<ThirdVodPlaybackVO> getVodFilePlaybackFlux(ThirdVodPlaybackQueryParam param);

}
