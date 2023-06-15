package com.feng.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    //@CrossOrigin // 允许跨域访问
    public String test() {
        System.out.println("demo ok!");
        return "demo ok!";
    }
}
