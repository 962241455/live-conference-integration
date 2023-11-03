package com.cmnt.dbpick.common.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 直播地址配置
 */
@Slf4j
@Data
@Configuration
public class LiveAddress {

    /**
     * 主讲端
     */
    @Value("${live.address.speech}")
    private String speechAddress;

    /**
     * 观众端
     */
    @Value("${live.address.viewer}")
    private String viewerAddress;

    /**
     * 观看直播前置页
     */
    @Value("${live.address.watch}")
    private String watchAddress;
   /**
     * 观看直播前置页
     */
    @Value("${live.address.meeting}")
    private String meetingAddress;




    public static String VIEWER_ADDRESS;
    public static String SPEECH_ADDRESS;
    public static String WATCH_ADDRESS;
    public static String MEETING_ADDRESS;

    @PostConstruct
    private void initEnv() {
        VIEWER_ADDRESS = viewerAddress;
        SPEECH_ADDRESS = speechAddress;
        WATCH_ADDRESS = watchAddress;
        MEETING_ADDRESS = meetingAddress;
        //log.info("直播地址配置 VIEWER_ADDRESS={}",VIEWER_ADDRESS);
    }
}
