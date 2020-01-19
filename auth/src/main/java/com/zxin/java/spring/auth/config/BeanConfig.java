package com.zxin.java.spring.auth.config;

import com.zxin.java.spring.auth.bean.ClientBuilder;
import com.zxin.java.spring.auth.bean.JdbcClientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;
import java.util.*;

@Configuration
public class BeanConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private volatile PasswordEncoder passwordEncoder;


    @Bean("defaultClientDetailService")
    public ClientDetailsService clientDetailsService(){
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder());
        return clientDetailsService;
//        InMemoryClientDetailsService clientDetailsService = new InMemoryClientDetailsService();
//        clientDetailsService.setClientDetailsStore(clients());
//        return clientDetailsService;
    }

    @Bean
    public UserDetailsService userDetailsService(){
//        ClientDetailsUserDetailsService clientDetailsUserDetailsService = new ClientDetailsUserDetailsService(clientDetailsService());
//        return clientDetailsUserDetailsService;
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        return jdbcUserDetailsManager;
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager(users());
        return userDetailsManager;
    }

    @Bean
    public TokenStore redisTokenStore(){
        String REDIS_SUFFIX = "oauth:";
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix(REDIS_SUFFIX);
        return redisTokenStore;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        if(passwordEncoder == null){
            synchronized (this){
                if(passwordEncoder == null) {
                    passwordEncoder = new BCryptPasswordEncoder();
                }
            }
        }
        return passwordEncoder;
    }

    private Map<String, ClientDetails> clients(){
        String AUTHORIZED_GRANT_TYPES = "authorization_code,refresh_token,password,client_credentials";
        String SCOPE = "all";
        Integer ACCESS_TOKEN_VALIDITY = 3600;
        Integer REFRESH_TOKEN_VALIDITY = 36000;

        ClientBuilder clientBuilder = JdbcClientDetails.builder().scopes(SCOPE)
                .authorizedGrantTypes(AUTHORIZED_GRANT_TYPES)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY)
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY);

        Map<String, ClientDetails> clients = new HashMap<>();
        ClientDetails client1 = clientBuilder.clientId("client1")
                .secret(passwordEncoder().encode("secret1")).build();
        ClientDetails client2 = clientBuilder.clientId("client2")
                .secret(passwordEncoder().encode("secret2")).build();
        clients.put("client1", client1);
        clients.put("client2", client1);

        return clients;
    }

    private Collection<UserDetails> users(){
        String role = "ROLE_ADMIN";
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        User user1 = new User("user1", "pwd1", authorities);
        User user2 = new User("user2", "pwd2", authorities);
        return Arrays.asList(user1, user2);
    }


}
