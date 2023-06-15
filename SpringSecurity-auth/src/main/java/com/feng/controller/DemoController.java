package com.feng.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/admin")  // 规定 有 ADMIN 角色才能访问
    public String admin() {
        return "admin ok";
    }

    @GetMapping("/user")  // 规定 有 USER 角色才能访问
    public String user() {
        return "user ok";
    }

    @GetMapping("/getInfo") // 规定有 READ_INFO 权限才能访问
    public String getInfo() {
        return "info ok";
    }
}
