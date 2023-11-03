package com.cmnt.dbpick.common.mq;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Mq 配置
 */
@Slf4j
@Data
@Configuration
public class RocketMQConfig {

    // 消费组
    @Value("${rocketmq.consumer.group}")
    private String consumerGroup;

    // 延迟消费组
    @Value("${rocketmq.delay_consumer.group}")
    private String delayConsumerGroup;
    // 延迟消费超时时间(毫秒)
    @Value("${rocketmq.delay_consumer.timeout}")
    private Integer delayConsumerTimeout;

    // 延迟消费 topic - 房间播放记录
//    @Value("${rocketmq.delay_consumer.topic.room_play_record}")
//    private String topicRoomPlayRecord;
    @Value("${rocketmq.delay_consumer.topic.room_play_record_delay_time}")
    private String topicRoomPlayRecordDelayTime;


//    // 延迟消费 topic - 房间热度值
//    @Value("${rocketmq.delay_consumer.topic.room_hot_scores}")
//    private String topicRoomHotScores;
//    @Value("${rocketmq.delay_consumer.topic.room_hot_scores_delay_time}")
//    private String topicRoomHotScoresDelayTime;


    // 消费组
    public static String CONSUMER_GROUP;


    // 延迟消费组
    public static String DELAY_CONSUMER_GROUP;
    public static Integer DELAY_CONSUMER_TIMEOUT;
//    public static String TOPIC_ROOM_PLAY_RECORD;
    public static String TOPIC_ROOM_PLAY_RECORD_DELAY_TIME;


//    public static String TOPIC_ROOM_HOT_SCORES;
//    public static String TOPIC_ROOM_HOT_SCORES_DELAY_TIME;


    @PostConstruct
    private void initEnv() {
        CONSUMER_GROUP = consumerGroup;

        DELAY_CONSUMER_GROUP = delayConsumerGroup;
        DELAY_CONSUMER_TIMEOUT = delayConsumerTimeout;
//        TOPIC_ROOM_PLAY_RECORD = topicRoomPlayRecord;
        TOPIC_ROOM_PLAY_RECORD_DELAY_TIME = topicRoomPlayRecordDelayTime;

//        TOPIC_ROOM_HOT_SCORES = topicRoomHotScores;
//        TOPIC_ROOM_HOT_SCORES_DELAY_TIME = topicRoomHotScoresDelayTime;

    }
}
