/**
 * Demo class
 *
 * @author 28021
 * @date 2022/7/25
 */
package com.cmnt.dbpick.common.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 腾讯配置项
 *
 * @author mr . wei
 * @date 2022/7/25
 */
@Slf4j
@Data
@Configuration
public class TencentConfig {

    // 服务器区域
    @Value("${tx.region}")
    private String region;

    // key 推流域名那能看到
    @Value("${tx.cloud.masterKey}")
    private String masterKey;

    @Value("${tx.cloud.secretId}")
    private String secretId;
    @Value("${tx.cloud.secretKey}")
    private String secretKey;

    // 推流域名
    @Value("${tx.cloud.push.url}")
    private String pushUrl;
    //生成推流地址鉴权key
    @Value("${tx.cloud.push.key}")
    private String pushKey;
    //生成推流地址超时时间
    @Value("${tx.cloud.push.timeout}")
    private Long pushTimeOut;

    // 播放域名
    @Value("${tx.cloud.playUrl}")
    private String playUrl;
    // 应用名称
    @Value("${tx.cloud.appName}")
    private String appName;


    @Value("${tx.cloud.sdk.appId}")
    private long sdkappid;
    @Value("${tx.cloud.key}")
    private String key;
    @Value("${tx.cloud.expire.time}")
    private int expireTime;


    //直播 拉流转推 回调地址
    @Value("${tx.live.pullStreamTaskCallbackUrl}")
    private String pullStreamTaskCallbackUrl;
    //视频转码模板
    @Value("${tx.live.transcodeTemplete}")
    private Long transcodeTemplete;


    // 存储桶名称
    @Value("${tx.cos.bucket_name}")
    private String bucketName;
    // 自定义文件夹
    @Value("${tx.cos.folders_prefix}")
    private String foldersPrefix;
    // 访问域名
    @Value("${tx.cos.access_url}")
    private String accessUrl;




    public static String REGION;
    public static String TX_CLOUD_SECRETID;
    public static String TX_CLOUD_SECRETKEY;

    public static String TX_CLOUD_PUSH_URL;
    public static String TX_PUSH_KEY;
    public static Long TX_PUSH_TIME_OUT;

    public static String TX_CLOUD_PLAYURL;
    public static String TX_CLOUD_APPNAME;
    public static long TX_CLOUD_SDK_APPID;
    public static String TX_CLOUD_KEY;
    public static int TX_CLOUD_EXPIRE_TIME;

    public static String TX_LIVE_PULL_TASK_CALLBACK_URL;
    public static Long TX_LIVE_TRANSCODE_TEMPLETE;

    public static String TX_COS_BUCKET_NAME;
    public static String TX_COS_FOLDERS_PREFIX;
    public static String TX_COS_ACCESS_URL;




    @PostConstruct
    private void initEnv() {
        REGION = region;
        TX_CLOUD_SECRETID = secretId;
        TX_CLOUD_SECRETKEY = secretKey;

        TX_CLOUD_PUSH_URL = pushUrl;
        TX_PUSH_KEY = pushKey;
        TX_PUSH_TIME_OUT = pushTimeOut;

        TX_CLOUD_PLAYURL = playUrl;
        TX_CLOUD_APPNAME = appName;
        TX_CLOUD_SDK_APPID = sdkappid;
        TX_CLOUD_KEY = key;
        TX_CLOUD_EXPIRE_TIME = expireTime;

        TX_LIVE_PULL_TASK_CALLBACK_URL = pullStreamTaskCallbackUrl;
        TX_LIVE_TRANSCODE_TEMPLETE = transcodeTemplete;

        TX_COS_BUCKET_NAME = bucketName;
        TX_COS_FOLDERS_PREFIX = foldersPrefix;
        TX_COS_ACCESS_URL = accessUrl;

        log.info("初始化腾讯云配置，secretId={}",TX_CLOUD_SECRETID);
    }
}
