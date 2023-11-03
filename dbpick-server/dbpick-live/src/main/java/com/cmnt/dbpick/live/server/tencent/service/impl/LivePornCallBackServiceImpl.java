package com.cmnt.dbpick.live.server.tencent.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.tx.tencent.response.live.LivePornCallBackResponse;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.live.server.mongodb.document.LivePornNotice;
import com.cmnt.dbpick.live.server.mongodb.repository.LivePornNoticeRepository;
import com.cmnt.dbpick.live.server.service.CallbackService;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import com.cmnt.dbpick.live.server.tencent.enums.LiveCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.event.LiveHandlerEventType;
import com.cmnt.dbpick.live.server.tencent.service.LiveCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 直播 鉴黄回调
 */
@Slf4j
@Component
@LiveHandlerEventType(LiveCallBackEnum.LIVE_PORN)
public class LivePornCallBackServiceImpl implements LiveCallBackService {

    @Autowired
    private LivePornNoticeRepository livePornNoticeRepository;

    @Autowired
    private CallbackService callbackServiceImpl;
    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;

    @Override
    public void execute(String jsonObjectStr) {
        log.info("直播 鉴黄 回调返回参数 str={}", jsonObjectStr);
        LivePornCallBackResponse res = JSON.parseObject(jsonObjectStr, LivePornCallBackResponse.class);
        log.info("直播 鉴黄 回调返回参数 response={}", res);
        StreamingRoomVO roomInfo = streamingRoomServiceImpl.detail(res.getStreamId());
        if(Objects.isNull(roomInfo)){
            log.error("直播 鉴黄 回调: 未查到房间信息 ....");
            callbackServiceImpl.saveLiveCallbackLog("","","", res);
            return;
        }
        //todo 处理 鉴黄 结果
        LivePornNotice pornNotice = initLivePornNotice(res);
        pornNotice.setAk(roomInfo.getAk());
        pornNotice.setThirdId(roomInfo.getThirdId());
        pornNotice.initSave(roomInfo.getCreateUser());
        log.info("保存鉴黄通知消息, pornNotice={}", pornNotice);
        livePornNoticeRepository.save(pornNotice);

    }

    /**
     * 组装鉴黄通知消息
     */
    private LivePornNotice initLivePornNotice(LivePornCallBackResponse param) {
        log.info("组装鉴黄通知消息, param={}",param);
        LivePornNotice pornNotice = LivePornNotice.builder().roomNo(param.getStreamId())
                .screenShotImg(param.getImg()).screenShotTime(param.getScreenshotTime())
                .label(param.getLabel()).subLabel(param.getLabel())
                .type(param.getType().get(0)).score(param.getScore().get(0))
                .ocrMsg(param.getOcrMsg()).suggestion(param.getSuggestion())
                .txApp(param.getApp()).txAppId(param.getAppid()).txAppName(param.getAppname())
                .txStreamParam(param.getStream_param()).sendTime(param.getSendTime()).build();
        return pornNotice;
    }

}
