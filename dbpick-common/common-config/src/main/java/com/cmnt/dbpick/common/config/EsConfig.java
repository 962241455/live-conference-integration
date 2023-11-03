package com.cmnt.dbpick.common.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Configuration
public class EsConfig {

    @Value("${spring.elasticsearch.rest.uris}")
    public String elasticsearchHost;

    @Value("${spring.elasticsearch.port}")
    public Integer elasticsearchPort;

    @Value("${spring.elasticsearch.scheme}")
    public String elasticsearchScheme;


    public static String ES_HOST;
    public static Integer ES_PORT;
    public static String ES_SCHEME;


    @PostConstruct
    private void initEnv() {
        ES_HOST = elasticsearchHost;
        ES_PORT = elasticsearchPort;
        ES_SCHEME = elasticsearchScheme;
    }
}

