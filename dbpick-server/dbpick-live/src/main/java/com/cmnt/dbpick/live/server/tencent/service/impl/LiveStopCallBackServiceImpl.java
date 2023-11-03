package com.cmnt.dbpick.live.server.tencent.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.enums.live.RoomStatusEnum;
import com.cmnt.dbpick.common.enums.live.RoomTypeEnum;
import com.cmnt.dbpick.common.tx.tencent.response.live.LivePushCallBackResponse;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.RedisUtils;
import com.cmnt.dbpick.live.server.service.CallbackService;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import com.cmnt.dbpick.live.server.tencent.enums.LiveCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.event.LiveHandlerEventType;
import com.cmnt.dbpick.live.server.tencent.service.LiveCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.cmnt.dbpick.common.constant.RedisKey.STOP_STREAM_HANDLE_TIME;

/**
 * 直播 断流回调
 */
@Slf4j
@Component
@LiveHandlerEventType(LiveCallBackEnum.LIVE_STOP)
public class LiveStopCallBackServiceImpl implements LiveCallBackService {

    @Autowired
    private CallbackService callbackServiceImpl;

    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;

    @Override
    public void execute(String jsonObjectStr) {
        log.info("直播 断流 回调返回参数 str={}", jsonObjectStr);
        LivePushCallBackResponse res = JSON.parseObject(jsonObjectStr, LivePushCallBackResponse.class);
        log.info("直播 断流 回调返回参数 response={}", res);
        StreamingRoomVO roomInfo = streamingRoomServiceImpl.detail(res.getStream_id());
        if(Objects.isNull(roomInfo)){
            log.error("直播 断流 回调: 未查到房间信息 ....");
            callbackServiceImpl.saveLiveCallbackLog("","","", res);
            return;
        }
//        if(StringUtils.equals(RoomTypeEnum.THIRD_PUSH.getValue(), roomInfo.getType())){
            log.info("直播 断流 回调: 更新房间状态 ....");
            streamingRoomServiceImpl.updateRoomStatus(roomInfo.getRoomNo(), RoomStatusEnum.LIVE_PAUSE.getValue(),"");
//        }
        callbackServiceImpl.saveLiveCallbackLog(roomInfo.getAk(),roomInfo.getThirdId(),roomInfo.getCreateUser(),res);
    }





}
