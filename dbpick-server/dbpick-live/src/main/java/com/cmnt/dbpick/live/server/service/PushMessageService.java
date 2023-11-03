package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.live.api.params.PushMessageParam;
import com.cmnt.dbpick.live.api.params.MessageParam;

/**
 * @description: 消息推送service
 * @author: shusong.liang
 * @create: 2022-08-12 08:43
 **/
public interface PushMessageService {

    /***
     * 创建消息推送
     * @param pushMessageParam
     */
    Boolean createMessagePush(PushMessageParam pushMessageParam);

    /**
     * 直播间发送消息
     * @param param
     * @return
     */
    Boolean sendLiveMag(MessageParam param);
    /**
     * 发起签到
     */
    Boolean initiateSign(MessageParam param);


    /**
     * 直播间确认签到
     */
    Boolean confirmSign(TokenInfo accessToken);
}
