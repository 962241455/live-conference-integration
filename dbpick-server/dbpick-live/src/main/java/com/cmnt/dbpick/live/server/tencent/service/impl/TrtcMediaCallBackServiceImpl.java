package com.cmnt.dbpick.live.server.tencent.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.enums.live.RoomStatusEnum;
import com.cmnt.dbpick.common.tx.tencent.response.trtc.RoomCallbackResponse;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.live.server.service.CallbackService;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import com.cmnt.dbpick.live.server.tencent.enums.TrtcCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.event.TrtcHandlerEventType;
import com.cmnt.dbpick.live.server.tencent.service.TrtcCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 实时音视频 媒体 回调
 */
@Slf4j
@Component
@TrtcHandlerEventType(TrtcCallBackEnum.TRTC_MEDIA)
public class TrtcMediaCallBackServiceImpl implements TrtcCallBackService {

    public final static String EVENT_TYPE_START_VIDEO = "201";//	开始推送视频数据
    public final static String EVENT_TYPE_STOP_VIDEO = "202";//	停止推送视频数据
    public final static String EVENT_TYPE_START_AUDIO = "203";//	开始推送音频数据
    public final static String EVENT_TYPE_STOP_AUDIO = "204";//	停止推送音频数据
    public final static String EVENT_TYPE_START_ASSIT = "205";//	开始推送辅路数据
    public final static String EVENT_TYPE_STOP_ASSIT = "206";//	停止推送辅路数据

    @Autowired
    private CallbackService callbackServiceImpl;

    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;

    @Override
    public void execute(String jsonObjectStr) {
        log.info("实时音视频 媒体 回调返回参数 str={}", jsonObjectStr);
        RoomCallbackResponse res = JSON.parseObject(jsonObjectStr, RoomCallbackResponse.class);
        log.info("实时音视频 媒体 回调返回参数 response={}", res);
        if(!(StringUtils.equals(res.getEventType(), EVENT_TYPE_START_VIDEO)
                || StringUtils.equals(res.getEventType(),EVENT_TYPE_START_AUDIO)
                || StringUtils.equals(res.getEventType(),EVENT_TYPE_START_ASSIT)  )){
            log.error("非媒体开始推送事件 ....");
            callbackServiceImpl.saveTrtcCallbackLog("","","",res);
            return;
        }
        StreamingRoomVO roomInfo = streamingRoomServiceImpl.detail(res.getEventInfo().getRoomId());
        if(Objects.isNull(roomInfo)){
            log.error("未查到房间信息 ....");
            callbackServiceImpl.saveTrtcCallbackLog("","","",res);
            return;
        }
        log.info("更新房间状态 为直播中....roomNo={}",roomInfo.getRoomNo());
        streamingRoomServiceImpl.updateRoomStatus(roomInfo.getRoomNo(), RoomStatusEnum.LIVE_ING.getValue(),"");
        callbackServiceImpl.saveTrtcCallbackLog(roomInfo.getAk(),roomInfo.getThirdId(),roomInfo.getCreateUser(),res);
    }

}
