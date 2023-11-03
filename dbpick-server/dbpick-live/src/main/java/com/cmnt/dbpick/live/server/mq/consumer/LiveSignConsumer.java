//package com.cmnt.dbpick.live.server.mq.consumer;
//
//import com.alibaba.fastjson.JSON;
//import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
//import com.cmnt.dbpick.common.exception.BizException;
//import com.cmnt.dbpick.common.mq.constant.MqTagEnum;
//import com.cmnt.dbpick.common.mq.constant.RocketMQConstant;
//import com.cmnt.dbpick.common.tx.tencent.TxCloudImUtil;
//import com.cmnt.dbpick.common.tx.tencent.response.TxMemberResponse;
//import com.cmnt.dbpick.common.user.UserBaseInfo;
//import com.cmnt.dbpick.live.api.params.MessageParam;
//import com.cmnt.dbpick.live.server.mongodb.document.LiveMessage;
//import com.cmnt.dbpick.live.server.mongodb.repository.LiveMessageRepository;
//import com.cmnt.dbpick.user.api.feign.UserClient;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.spring.annotation.ConsumeMode;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.annotation.SelectorType;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Objects;
//
///**
// * 直播签到消费
// */
//@Component
//@RocketMQMessageListener(
//        topic = RocketMQConstant.TOPIC,// 1.topic：消息的发送者使用同一个topic
//        consumerGroup = RocketMQConstant.TIME_OUT_ORDER_GROUP,// 2.group：不用和生产者group相同 ( 在RocketMQ中消费者和发送者组没有关系 )
//        selectorExpression = RocketMQConstant.TAG_SIGN,// 3.tag：设置为 * 时，表示全部。
//        selectorType = SelectorType.TAG, consumeMode= ConsumeMode.CONCURRENTLY)
//@Slf4j
//public class LiveSignConsumer implements RocketMQListener, RocketMQPushConsumerLifecycleListener {
//
//    @Autowired
//    private TxCloudImUtil txCloudImUtil;
//    @Autowired
//    private UserClient userClient;
//
//    @Autowired
//    private LiveMessageRepository liveMessageRepository;
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public void onMessage(Object o) {
//        log.debug("处理发起签到消息：{}", o);
//        MessageParam messageParam = JSON.parseObject(o.toString(), MessageParam.class);
//        try {
//            TxMemberResponse result = txCloudImUtil.imGroupSystemPush(messageParam.getRoomNo(), messageParam.getSignMsg());
//            log.debug("处理发起签到结果：{}",result);
//            LiveMessage liveMessage = LiveMessage.builder()
//                    .roomNo(messageParam.getRoomNo()).initiatorUserId(messageParam.getInitiatorUserId())
//                    .msgType(messageParam.getMsgType()).msgInfo(messageParam.getMsgInfo())
//                    .initiateTime(DateUtil.getTimeStrampSeconds())
//                    .build();
//            //查询用户信息
//            ResponsePacket<UserBaseInfo> userBaseInfo = userClient.findUserBaseInfo(messageParam.getInitiatorUserId());
//            if (Objects.nonNull(userBaseInfo) && Objects.nonNull(userBaseInfo.getData())){
//                UserBaseInfo data = userBaseInfo.getData();
//                liveMessage.setInitiatorUserName(data.getUserName());
//                liveMessage.setInitiatorUserAvatar(data.getUserAvatar());
//            }
//            liveMessage.initSave("");
//            log.debug("保存发起签到消息：{}",result);
//            liveMessageRepository.save(liveMessage);
//        }catch (Exception e){
//            e.printStackTrace();
//            log.error(e.getMessage());
//            throw new BizException("处理发起签到消息异常");
//        }
//    }
//
//
//    @Override
//    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
//        StringBuilder builder = new StringBuilder();
//        String tag = builder.append(MqTagEnum.LIVE_SERVER_TOPIC.getTag()).append("-").append(MqTagEnum.TAG_SIGN.getTag()).toString();
//        defaultMQPushConsumer.setInstanceName(tag);
//    }
//}
