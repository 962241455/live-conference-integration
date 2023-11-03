package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.enums.BizExceptionEnum;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.utils.ValidateAssert;
import com.cmnt.dbpick.live.api.params.PushMessageParam;
import com.cmnt.dbpick.live.api.params.MessageParam;
import com.cmnt.dbpick.live.server.service.PushMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 消息推送Controller
 * @author: shusong.liang
 * @create: 2022-08-12 15:29
 **/
@Slf4j
@RestController
@Api(tags = {"消息推送相关API"})
@RequestMapping("/push_message")
public class PushMessageController extends BaseController {

    @Autowired
    private PushMessageService pushMessageService;

    @ApiOperation("创建消息推送")
    @PostMapping("/message/push")
    public ResponsePacket createMessagePush(@RequestBody PushMessageParam pushMessageParam) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL.getErrMsg(), pushMessageParam);


        return ResponsePacket.onSuccess(pushMessageService.createMessagePush(pushMessageParam));

    }


    @ApiOperation("直播间发送消息")
    @PostMapping("/send")
    public ResponsePacket<Boolean> sendLiveMag(@Validated @RequestBody MessageParam param) {
//        TokenInfo accessToken = getAccessToken();
//        param.setInitiatorUserId(accessToken.getUserId());
        log.info("直播间发送消息: param={}",param);
        return ResponsePacket.onSuccess(pushMessageService.sendLiveMag(param));
    }


    @ApiOperation("直播间确认签到")
    @PostMapping("/sign/confirm")
    public ResponsePacket<Boolean> confirmSign() {
        TokenInfo accessToken = getAccessToken();
        log.info("直播间确认签到: accessToken={}",accessToken);
        return ResponsePacket.onSuccess(pushMessageService.confirmSign(accessToken));
    }

}
