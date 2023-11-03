package com.cmnt.dbpick.manager.server;

import com.cmnt.dbpick.common.eventutils.EventBus;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executors;

@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.cmnt.dbpick"})
@MapperScan(basePackages = "com.cmnt.dbpick.manager.server.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class ManagerApplication {

    public static void main(String[] args) {
        EventBus.create(Executors.newFixedThreadPool(5, r -> {
            Thread thread = new Thread(r);
            thread.setName("eventBus");
            thread.setDaemon(true);
            return thread;
        }));
        SpringApplication springApplication = new SpringApplication(ManagerApplication.class);
        springApplication.setAllowBeanDefinitionOverriding(true);
        springApplication.run(args);
    }

}
