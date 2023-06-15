package com.feng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class HelloController {


    @Autowired
    private AuthenticationManager authenticationManager;
    @GetMapping("/hello")
    public String hello() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        new Thread(()-> {
            System.out.println("子线程:" + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        }).start();
        System.out.println(principal + " " + authorities);
        System.out.println("Spring Secutiry");
        return "Hello, Spring Security!";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "Hello, Spring security 2";
    }
}
