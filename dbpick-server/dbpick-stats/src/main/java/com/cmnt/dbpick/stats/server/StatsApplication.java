package com.cmnt.dbpick.stats.server;

import com.cmnt.dbpick.common.eventutils.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.concurrent.Executors;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.cmnt.dbpick.**"})
@SpringBootApplication(scanBasePackages = {"com.cmnt.dbpick"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class StatsApplication {

    public static void main(String[] args) {
        EventBus.create(Executors.newFixedThreadPool(5, r -> {
            Thread thread = new Thread(r);
            thread.setName("eventBus");
            thread.setDaemon(true);
            return thread;
        }));
        SpringApplication springApplication = new SpringApplication(StatsApplication.class);
        springApplication.setAllowBeanDefinitionOverriding(true);
        springApplication.run(args);
    }

}
