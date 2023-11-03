package com.cmnt.dbpick.live.server.tencent.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.tx.tencent.response.live.LivePushTaskCallBackResponse;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomVideoLive;
import com.cmnt.dbpick.live.server.service.CallbackService;
import com.cmnt.dbpick.live.server.service.LiveVideosService;
import com.cmnt.dbpick.live.server.tencent.enums.LiveCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.event.LiveHandlerEventType;
import com.cmnt.dbpick.live.server.tencent.service.LiveCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 直播 拉流转推回调
 */
@Slf4j
@Component
@LiveHandlerEventType(LiveCallBackEnum.LIVE_PUSH_TASK)
public class LivePushTaskCallBackServiceImpl implements LiveCallBackService {

    @Autowired
    private CallbackService callbackServiceImpl;

    @Autowired
    private LiveVideosService liveVideosServiceImpl;

    @Override
    public void execute(String jsonObjectStr) {
        log.info("直播 拉流转推 回调返回参数 str={}", jsonObjectStr);
        LivePushTaskCallBackResponse res = JSON.parseObject(jsonObjectStr, LivePushTaskCallBackResponse.class);
        log.info("直播 拉流转推 回调返回参数 response={}", res);
        String taskId = res.getTask_id();
        StreamingRoomVideoLive videoInfo = liveVideosServiceImpl.findVideoByTaskId(taskId);
        if(Objects.isNull(videoInfo)){
            log.error("直播 拉流转推 回调：未查询到视频信息 ....");
            callbackServiceImpl.saveLiveCallbackLog("","","",res);
            return;
        }
        liveVideosServiceImpl.updateStatus(taskId, res.getCallback_event());
        callbackServiceImpl.saveLiveCallbackLog(videoInfo.getAk(),videoInfo.getThirdId(),videoInfo.getCreateUser(),res);
    }
}
