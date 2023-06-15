package com.feng.controller;


import com.feng.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hello")
public class AuthorizeMethodController {


    @PreAuthorize("hasRole('ADMIN') and authentication.name == 'root'")
    @GetMapping
    public String hello() {
        return "hello";
    }

    @PreAuthorize("authentication.name==#name")
    @GetMapping("/name")
    public String hello(String name) {
        return "hello:" + name;
    }

    @PreFilter(value = "filterObject.id%2!=0", filterTarget = "users")
    @PostMapping("/users")
    public void addUsers(@RequestBody List<User> users) {
        System.out.println("Users = " + users);
    }
}
