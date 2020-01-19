package com.zxin.java.spring.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/resource")
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }


}
