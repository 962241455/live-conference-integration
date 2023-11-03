package com.cmnt.dbpick.common.mq;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.mq.properties.RocketEnhanceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Component
@Slf4j
public class RocketMQProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private RocketEnhanceProperties rocketEnhanceProperties;

    /**
     * 发送同步消息
     */
    public SendResult sendSyncMsg(String topic, Object msgBody) {
        log.info("发送同步消息【syncSendMsg】 topic={}", topic);
        SendResult sendResult = rocketMQTemplate.syncSend(reBuildTopic(topic), MessageBuilder.withPayload(msgBody).build());
        log.info("发送完成 sendResult={}", JSON.toJSONString(sendResult));
        return sendResult;
    }

    /**
     * 发送异步消息
     */
    public void sendAsyncMsg(String topic, String msgBody) {
        log.info("发送异步消息【sendAsyncMsg】 topic={}, msgBody={}", topic, msgBody);
        rocketMQTemplate.asyncSend(reBuildTopic(topic), MessageBuilder.withPayload(msgBody).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 处理消息发送成功逻辑
                log.info("发送成功 sendResult={}", JSON.toJSONString(sendResult));
            }

            @Override
            public void onException(Throwable throwable) {
                // 处理消息发送异常逻辑
                log.info("发送失败 throwable={}", JSON.toJSONString(throwable));
            }
        });
    }

    /**
     * 发送延时消息（上面的发送同步消息，delayLevel的值就为0，因为不延时）
     * 在start版本中 延时消息一共分为18个等级分别为：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    public SendResult sendDelayMsg(String topic, String msgBody, int delayLevel) {
        log.info("发送延时消息【sendDelayMsg】 topic={}, msgBody={}，delayLevel={}", topic, msgBody, delayLevel);
        SendResult result = rocketMQTemplate.syncSend(reBuildTopic(topic),
                MessageBuilder.withPayload(msgBody).build(),
                RocketMQConfig.DELAY_CONSUMER_TIMEOUT, delayLevel);
        log.info("发送完成 sendResult={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 发送单向消息（只负责发送消息，不等待应答，不关心发送结果，如日志）
     */
    public void sendOneWayMsg(String topic, String msgBody) {
        log.info("发送单向消息【sendOneWayMsg】 topic={}", topic);
        rocketMQTemplate.sendOneWay(reBuildTopic(topic), MessageBuilder.withPayload(msgBody).build());
    }


    /**
     * 根据系统上下文自动构建隔离后的topic
     * 构建目的地
     */
    public String buildDestination(String topic, String tag) {
        topic = reBuildTopic(topic);
        return topic + ":" + tag;
    }

    /**
     * 根据环境重新隔离topic
     *
     * @param topic 原始topic
     */
    private String reBuildTopic(String topic) {
        if (rocketEnhanceProperties.isEnabledIsolation() && StringUtils.hasText(rocketEnhanceProperties.getEnvironment())) {
            return topic + "_" + rocketEnhanceProperties.getEnvironment();
        }
        return topic;
    }


}
