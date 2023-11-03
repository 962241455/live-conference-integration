package com.cmnt.dbpick.common.config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 腾讯录制配置
 */
@Slf4j
@Data
@Configuration
public class TxRecordConfig {

    /** 录制模式。
     *  1：单流录制，分别录制房间的订阅UserId的音频和视频，将录制文件上传至云存储；
     *  2：混流录制，将房间内订阅UserId的音视频混录成一个音视频文件，将录制文件上传至云存储； */
    @Value("${tx.record.mode}")
    private Long recordMode;

    /** 房间内持续没有主播的状态超过MaxIdleTime的时长，自动停止录制，
     *  单位：秒。默认值为 30 秒，该值需大于等于 5秒，且小于等于 86400秒(24小时) */
    @Value("${tx.record.max_idle_time}")
    private Long maxIdleTime;


    /** 媒体文件过期时间，为当前时间的绝对过期时间
     *  单位：秒。86400-保存一天，0-永久保存，默认永久保存。*/
    @Value("${tx.record.video.expire_time}")
    private Long videoExpireTime;


    /** 视频的宽度值，单位为像素，默认值360。不能超过1920，与height的乘积不能超过1920*1080。*/
    @Value("${tx.record.video.width}")
    private Long videoWidth;
    /** 视频的高度值，单位为像素，默认值640。不能超过1920，与width的乘积不能超过1920*1080。*/
    @Value("${tx.record.video.height}")
    private Long videoHeight;
    /** 视频的帧率，范围[1, 60]，默认15。*/
    @Value("${tx.record.video.fps}")
    private Long videoFps;
    /** 视频的码率,单位是bps，范围[64000, 8192000]，默认550000bps。*/
    @Value("${tx.record.video.bit_rate}")
    private Long videoBitRate;
    /** 视频关键帧时间间隔，单位秒，默认值10秒*/
    @Value("${tx.record.video.gop}")
    private Long videoGop;


    /** 直播录制模板 */
    @Value("${tx.record.live_template}")
    private Long liveTemplate;
    /** 单场直播录制时长(单位秒, 不能超过24小时-86400 秒) */
    @Value("${tx.record.live_max_time}")
    private Long liveMaxTime;

    /**
     * 录制的媒体流类型： 0-录制音频+视频流（默认）；  1-仅录制音频流； 2-仅录制视频流
     */
    public static Long RECORD_STREAM_TYPE = 0L;

    public static Long RECORD_MODE;
    public static Long RECORD_MAX_IDLE_TIME;
    public static Long RECORD_VIDEO_EXPIRE_TIME;

    public static Long RECORD_VIDEO_WIDTH;
    public static Long RECORD_VIDEO_HEIGHT;
    public static Long RECORD_VIDEO_FPS;
    public static Long RECORD_VIDEO_BIT_RATE;
    public static Long RECORD_VIDEO_GOP;

    public static Long LIVE_RECORD_TEMPLATE;
    public static Long LIVE_RECORD_MAX_TIME;


    @PostConstruct
    private void initEnv() {
        RECORD_MODE = recordMode;
        RECORD_MAX_IDLE_TIME = maxIdleTime;
        RECORD_VIDEO_EXPIRE_TIME = videoExpireTime;

        RECORD_VIDEO_WIDTH = videoWidth;
        RECORD_VIDEO_HEIGHT = videoHeight;
        RECORD_VIDEO_FPS = videoFps;
        RECORD_VIDEO_BIT_RATE = videoBitRate;
        RECORD_VIDEO_GOP = videoGop;

        LIVE_RECORD_TEMPLATE = liveTemplate;
        LIVE_RECORD_MAX_TIME = liveMaxTime;
    }
}
