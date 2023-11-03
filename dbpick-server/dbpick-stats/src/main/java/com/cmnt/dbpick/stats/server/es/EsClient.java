package com.cmnt.dbpick.stats.server.es;

import com.cmnt.dbpick.common.config.EsConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@DependsOn("esConfig")
public class EsClient {

    @Bean
    public RestHighLevelClient client() {
        /*String esUrl = EsConfig.ES_HOST+":"+EsConfig.ES_PORT;
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(esUrl)//elasticsearch地址
                .build();
        log.info("初始化es配置：{}",esUrl);
        return RestClients.create(clientConfiguration).rest();*/
        log.info("初始化es配置：host={} ; port={} ",EsConfig.ES_HOST, EsConfig.ES_PORT);
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(
                        EsConfig.ES_HOST, EsConfig.ES_PORT, EsConfig.ES_SCHEME
                )
        );
        builder.setHttpClientConfigCallback(
                httpClientBuilder -> httpClientBuilder.setKeepAliveStrategy(
                        (httpResponse, httpContext) -> 1000 * 60
                )
        );
        return new RestHighLevelClient(builder);
    }

}
