package com.cmnt.dbpick.stats.server.xxljob;

import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.stats.server.service.PlaybackVodFluxService;
import com.cmnt.dbpick.stats.server.service.RoomFluxStatsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoomInfoRecordJob {

    @Autowired
    private RoomFluxStatsService roomFluxStatsServiceImpl;
    @Autowired
    private PlaybackVodFluxService playbackVodFluxServiceImpl;


    /**
     * 查询天维度每条流的播放数据，包括总流量等。
     * 凌晨3点之后执行
     */
    @XxlJob("dealRoomStatsJob")
    public ReturnT<String> dealStreamDayPlayInfoJob(String param) {
        String nowDateStr = DateUtil.nowDateTime(DateUtil.Y_M_D_HMS);
        log.info("xxlJob-start>>>dealStreamDayPlayInfoJob:查询昨天每条流的播放总流量...param={}, 执行时间：{}",param,nowDateStr);
        roomFluxStatsServiceImpl.saveRoomFluxStats();
        log.info("xxlJob-end>>>dealStreamDayPlayInfoJob:查询昨天每条流的播放总流量...结束...");
        return ReturnT.SUCCESS;
    }


    /** todo 配置xxljob
     * 查询昨天的回放流量情况。
     * 每天晚上10点执行
     */
    @XxlJob("dealPlaybackVideoFluxJob")
    public ReturnT<String> dealPlaybackVideoFluxJob(String param) {
        String nowDateStr = DateUtil.nowDateTime(DateUtil.Y_M_D_HMS);
        log.info("xxlJob-start>>>dealPlaybackVideoFluxJob:查询昨天的回放流量情况...param={}, 执行时间：{}",param,nowDateStr);
        playbackVodFluxServiceImpl.savePlaybackVideoFlux();
        log.info("xxlJob-end>>>dealPlaybackVideoFluxJob:查询昨天的回放流量情况(每天晚上10点执行)...结束...");
        return ReturnT.SUCCESS;
    }


    /** todo 配置xxljob
     * 查询每天 TRTC音视频用量。
     * 凌晨3点之后执行
     */
    @XxlJob("dealTrtcUsageJob")
    public ReturnT<String> dealAllTrtcUsageJob(String param) {
        String nowDateStr = DateUtil.nowDateTime(DateUtil.Y_M_D_HMS);
        log.info("xxlJob-start>>>dealTrtcUsageJob:查询昨天TRTC音视频用量总和...param={}, 执行时间：{}",param,nowDateStr);
        roomFluxStatsServiceImpl.saveAllTrtcUsage();
        log.info("xxlJob-end>>>dealTrtcUsageJob:查询昨天TRTC音视频用量总和...结束...");
        return ReturnT.SUCCESS;
    }

}
