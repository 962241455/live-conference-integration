package com.cmnt.dbpick.common.upload.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * obs 配置项
 *
 * @author mr . wei
 * @date 2022/7/25
 */
@Slf4j
@Data
@Configuration
public class ObsConfig {

    @Value("${spring.obs.access_key_id}")
    private String access_key_id;

    @Value("${spring.obs.secret_access_key}")
    private String secret_access_key;

    @Value("${spring.obs.server_url}")
    private String server_url;

    @Value("${spring.obs.playCdnUrl}")
    private String playCdnUrl;




    public static String ACCESS_KEY;
    public static String SECRET_KEY;
    public static String SERVER_URL;
    public static String PLAY_CDN_URL;




    @PostConstruct
    private void initEnv() {
        ACCESS_KEY = access_key_id;
        SECRET_KEY = secret_access_key;
        SERVER_URL = server_url;
        PLAY_CDN_URL = playCdnUrl;
    }
}
