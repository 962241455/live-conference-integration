package com.cmnt.dbpick.gateway.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableDiscoveryClient
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GatewayApplication {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(GatewayApplication.class);
        springApplication.setAllowBeanDefinitionOverriding(true);
        springApplication.run(args);

    }
}
