package com.feng.springsecrity02.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 对 springmvc 进行自定义配置
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 配置无逻辑视图跳转
     * 浏览器请求 /login.html 时候，跳转到视图 login
     * 浏览器请求 /index.hmtl 时候，跳转到视图 index
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/index.html").setViewName("index");
    }
}
