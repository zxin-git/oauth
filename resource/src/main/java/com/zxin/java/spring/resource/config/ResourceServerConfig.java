package com.zxin.java.spring.resource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * @author zxin
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("res_id").tokenServices(remoteTokenServices());
    }

    @Bean
    public RemoteTokenServices remoteTokenServices() {
        RemoteTokenServices services = new RemoteTokenServices();
//        services.setClientId("client1");
//        services.setClientSecret("secret1");
        services.setClientId("client2");
        services.setClientSecret("secret2");
        services.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        return services;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/actuator**").authenticated()
                .antMatchers("/resource/**").authenticated();
    }
}
