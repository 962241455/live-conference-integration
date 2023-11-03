package com.cmnt.dbpick.live.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.enums.live.MessageTypeEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.tx.tencent.TxCloudImUtil;
import com.cmnt.dbpick.common.tx.tencent.response.TxMemberResponse;
import com.cmnt.dbpick.common.user.UserBaseInfo;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.ForkJoinUtils;
import com.cmnt.dbpick.live.api.params.PushMessageParam;
import com.cmnt.dbpick.live.api.params.MessageParam;
import com.cmnt.dbpick.live.server.mongodb.document.LiveSignRecord;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveSignRecordRepository;
import com.cmnt.dbpick.live.server.service.PushMessageService;
import com.cmnt.dbpick.user.api.feign.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @description: 消息推送service
 * @author: shusong.liang
 * @create: 2022-08-12 08:43
 **/
@Slf4j
@Service
public class PushMessageServiceImpl implements PushMessageService {

    @Autowired
    private TxCloudImUtil txCloudImUtil;
    @Autowired
    private UserClient userClient;

    @Autowired
    private LiveSignRecordRepository liveSignRecordRepository;

    @Override
    public Boolean createMessagePush(PushMessageParam pushMessageParam) {

        CompletableFuture.runAsync(() -> {
            log.info("异步处理 开始》| pushMessageParam: {} ", pushMessageParam);
            TxMemberResponse response = txCloudImUtil.imGroupSystemPush("10024","","");
            log.info("异步处理 结束》 | response: {} ", response);
        }, ForkJoinUtils.getCommonPool());

        return Boolean.TRUE;
    }


    /**
     * 直播间发送消息
     * @param param
     * @return
     */
    @Override
    public Boolean sendLiveMag(MessageParam param) {
        MessageTypeEnum messageType = MessageTypeEnum.getByValue(param.getMsgType());
        switch (messageType){
            case UNKNOWN:
                throw new BizException(messageType.getDesc());
            /*case MSG_SIGN:
                initiateSign(param);
                break;
            case MSG_ANNOUNCEMENT:
                mqProducerService.sendTagMsg(JSON.toJSONString(param),MqTagEnum.TAG_ANNOUNCEMENT.getTag());
                break;
            case MSG_POPUP:
                mqProducerService.sendTagMsg(JSON.toJSONString(param),MqTagEnum.TAG_POPUP.getTag());
                break;
            case MSG_QUESTION:
                mqProducerService.sendTagMsg(JSON.toJSONString(param),MqTagEnum.TAG_QUESTION.getTag());
                break;
            case MSG_EXAM:
                mqProducerService.sendTagMsg(JSON.toJSONString(param),MqTagEnum.TAG_EXAM.getTag());
                break;*/
            default:
                break;
        }
        return Boolean.TRUE;
    }


    /**
     * 发起签到
     */
    @Override
    public Boolean initiateSign(MessageParam param) {
        log.info("发起签到 开始 param: {} ", param);
        /*MqDelayLevelEnum levelEnum = MqDelayLevelEnum.lookup(param.getDelayLevel());
        switch (levelEnum){
            case NOW:
                mqProducerService.sendTagMsg(JSON.toJSONString(param),MqTagEnum.TAG_SIGN.getTag());
                break;
            default:
                mqProducerService.sendDelayMsg(JSON.toJSONString(param),levelEnum.getLevel(), MqTagEnum.TAG_SIGN.getTag());
                break;
        }*/
        return Boolean.TRUE;
    }

    /**
     * 直播间确认签到
     */
    @Override
    public Boolean confirmSign(TokenInfo accessToken) {
        LiveSignRecord signRecord = LiveSignRecord.builder()
                .roomNo(accessToken.getRoomNo()).userId(accessToken.getUserId())
                .signTime(DateUtil.getTimeStrampSeconds()).build();
        //查询用户信息
        ResponsePacket<UserBaseInfo> resilt = userClient.findUserBaseInfo(accessToken.getUserId());
        if (Objects.nonNull(resilt) && Objects.nonNull(resilt.getData())){
            UserBaseInfo data = resilt.getData();
            signRecord.setUserAvatar(data.getUserAvatar());
            signRecord.setUserName(data.getUserName());
        }
        signRecord.initSave("");
        liveSignRecordRepository.save(signRecord);
        return null;
    }


}
