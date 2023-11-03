package com.cmnt.dbpick.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @description SwaggerConfig
 * @author zk
 * @date 2020-11-16
 */
 @Configuration
 @EnableSwagger2
 @EnableKnife4j
public class SwaggerConfig {

    public static boolean isProd = false;


    //@Value("${swagger.enable}")
    //private boolean enableSwagger;
    /**
     * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     * @return
     */
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cmnt.dbpick"))
                /*.apis(Predicates.or(
                        RequestHandlerSelectors.basePackage("com.cmnt.dbpick.live.server.controller"),
                        RequestHandlerSelectors.basePackage("com.cmnt.dbpick.manager.server.controller"),
                        RequestHandlerSelectors.basePackage("com.cmnt.dbpick.stats.server.controller"),
                        RequestHandlerSelectors.basePackage("com.cmnt.dbpick.third.server.controller"),
                        RequestHandlerSelectors.basePackage("com.cmnt.dbpick.user.server.controller"),
                        RequestHandlerSelectors.basePackage("com.cmnt.dbpick.transcoding.server.controller"),
                        RequestHandlerSelectors.basePackage("com.cmnt.dbpick.oauth2.server.controller")
                        ))*/
                .paths(PathSelectors.any())
                .build()
                .enable(!isProd);
    }

    /**
     * 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("Application Remote mall Restful API Documents")
                // 创建人信息
                .contact(new Contact("cmnt",  "https://www.cmnt.com",  ""))
                // 版本号
                .version("1.0")
                // 描述
                .description("API 描述")
                .build();
    }
}
