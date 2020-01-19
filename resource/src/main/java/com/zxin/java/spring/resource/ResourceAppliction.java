package com.zxin.java.spring.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
public class ResourceAppliction {

    public static void main(String[] args) {
        SpringApplication.run(ResourceAppliction.class, args);
    }
}
