package com.cmnt.dbpick.common.mq.constant;

/**
 * mq常量
 */
public class RocketMQConstant {

    /**
     * 消费组
     */
    public static final String  CONSUMER_GROUP = "live-consumer-group";

    /**
     * AOP 日志切面
     */
    public static final String  CONFIG_SYSLOG_AOP_TOPIT = "config-consumer-aop-sys-Log";
    public static final String  CONFIG_SYSLOG_AOP_GROUP = "config-consumer-aop-group";


    /**
     * 延迟消费组
     */
    public static final String DELAY_CONSUMER_GROUP = "live-delay-consumer-group";

    /**
     * 延迟消费 topic
     */
    // 直播间播放记录数据统计
    public static final String DELAY_TOPIC_ROOM_PLAY_RECORD = "room-play-record-stats";

    /**
     * 延迟消费 topic
     */
    // 直播间播放记录数据统计
    public static final String DELAY_TOPIC_ROOM_HOT_SCORES = "room-hot-scores-stats";


    /**
     * 视频转码发起 topic
     *
     * start 开始
     * end 完成
     */
    public static final String DELAY_TOPIC_VIDEO_TRANSCODING_START = "room-video-transcoding-start";
    public static final String DELAY_TOPIC_VIDEO_TRANSCODING_END = "room-video-transcoding-end";


    /**
     * 会议相关
     */
    public static final String DELAY_TOPIC_VIDEO_MEETING_START = "delay-topik-video-meeting";




}
