package com.cmnt.dbpick.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description 描述
 * @date 2022-08-11 15:32
 */
@Configuration
public class RedissonConfig {

    @Value("${redisson.address}")
    private String addressUrl;

    @Value("${redisson.password}")
    private String password;

    @Value("${redisson.database}")
    private Integer database;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient getRedisson() throws Exception{
        RedissonClient redisson = null;
        Config config = new Config();
        config.useSingleServer()
                .setAddress(addressUrl)
                .setDatabase(database)
                .setPassword(password);
        redisson = Redisson.create(config);
        return redisson;
    }

}
