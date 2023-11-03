package com.cmnt.dbpick.live.server.xxljob;

import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.tx.tencent.TxCloudImUtil;
import com.cmnt.dbpick.common.utils.RedisUtils;
import com.cmnt.dbpick.live.api.vo.redis.LiveRecordTaskVO;
import com.cmnt.dbpick.live.api.vo.redis.LiveStreamStopVO;
import com.cmnt.dbpick.live.server.service.LiveRecordTaskLogService;
import com.cmnt.dbpick.live.server.service.LiveVoteService;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
public class RoomInfoJob {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TxCloudImUtil txCloudImUtil;

    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;
    @Autowired
    private LiveRecordTaskLogService liveRecordTaskLogServiceImpl;
    @Autowired
    private LiveVoteService liveVoteServiceImpl;

    /**
     * 解散聊天室
     * @return
     */
    @XxlJob("dealImGroupJob")
    public ReturnT<String> dealImGroupJob(String param) {
        log.info("xxlJob-start>>>dealImGroupJob:解散聊天室...开始...param={}",param);
        Set roomNoSet = redisUtils.getSet(RedisKey.DESTROY_IM_GROUP);
        roomNoSet.forEach(roomNo -> {
            txCloudImUtil.destroyImGroup(String.valueOf(roomNo));
            liveVoteServiceImpl.stopVoteByRoomNo(String.valueOf(roomNo), ""); //im 回调里操作
        });
        log.info("xxlJob-end>>>dealImGroupJob:解散聊天室...结束...");
        return ReturnT.SUCCESS;
    }



    /**
     * 停止直播录制任务
     * @return
     */
    @XxlJob("stopLiveRecordTaskJob")
    public ReturnT<String> stopLiveRecordTaskJob(String param) {
        log.info("xxlJob-start>>>stopLiveRecordTaskJob:停止直播录制任务...开始...param={}",param);
        Map<Object, Object> taskMap = redisUtils.hmGetAll(RedisKey.LIVE_RECORD_TASK);
        taskMap.forEach((k,v)->{
            if(Objects.nonNull(v)){
                liveRecordTaskLogServiceImpl.stopLiveRecordTask(
                        JSONObject.toJavaObject(JSONObject.parseObject(v.toString()), LiveRecordTaskVO.class));
            }
        });
        log.info("xxlJob-end>>>stopLiveRecordTaskJob:停止直播录制任务...结束...");
        return ReturnT.SUCCESS;
    }


    /**
     * 处理直播停止之后的房间状态
     * @return
     */
    @XxlJob("stopLiveRoomStatusJob")
    public ReturnT<String> stopLiveRoomStatusJob(String param) {
        log.info("xxlJob-start>>>stopLiveRoomStatusJob:处理直播停止之后的房间状态...开始...param={}",param);
        Map<Object, Object> taskMap = redisUtils.hmGetAll(RedisKey.STOP_STREAM_ROOM_NO);
        taskMap.forEach((k,v)->{
            if(Objects.nonNull(v)){
                streamingRoomServiceImpl.stopLiveRoomStatus(
                        JSONObject.toJavaObject(JSONObject.parseObject(v.toString()), LiveStreamStopVO.class));
            }
        });
        log.info("xxlJob-end>>>stopLiveRoomStatusJob:处理直播停止之后的房间状态...结束...");
        return ReturnT.SUCCESS;
    }

}
