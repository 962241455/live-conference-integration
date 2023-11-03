package com.cmnt.dbpick.gateway.server.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

/**
 * 网关白名单配置
 */
@Data
@Configuration
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "secure.white")
public class WhitelistConfig {

    private List<String> lists;

}
