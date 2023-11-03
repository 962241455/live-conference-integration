package com.cmnt.dbpick.gateway.server.config;//package com.cmnt.live.security;

/*import com.cmnt.live.security.ClientResources;
import com.cmnt.live.security.UserInfoTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.context.request.RequestContextListener;*/

/**
 * 集成oauth相关代码，从server项目中拷贝
 *
 * @author zk
 * @Description: 资源和访问权限配置
 * @date 2020年10月15日
 */
//@Configuration
//@EnableOAuth2Client
//@EnableResourceServer
public class ResourceServerConfig {

    /*@Autowired
    ClientResources pixbe;

    @Autowired
    ResourceServerTokenServices tokenService;

    @Bean
    public ResourceServerTokenServices tokenService() {
        return new UserInfoTokenService(pixbe.getResource().getUserInfoUri(), pixbe.getClient().getClientId());
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Bean
    @ConfigurationProperties("pixbe.auth")
    ClientResources pixbe() {
        return new ClientResources();
    }*/
}
