package com.feng.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig {

    private final FindByIndexNameSessionRepository findByIndexNameSessionRepository;

    public SecurityConfig(FindByIndexNameSessionRepository sessionRepository) {
        this.findByIndexNameSessionRepository = sessionRepository;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests().anyRequest().authenticated()
                .and().formLogin()
                .and().rememberMe()
                .and().csrf().disable()
                .sessionManagement()
                .maximumSessions(1)
                //.expiredUrl("/login");  // 会话过期之后的处理
                .expiredSessionStrategy(event -> {
                    HttpServletResponse response = event.getResponse();
                    response.setContentType("application/json;charset=UTF-8");
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", 500);
                    result.put("msg", "当前会话已经失效，请重新登录!");
                    String s = new ObjectMapper().writeValueAsString(result);
                    response.getWriter().println(s);
                    response.flushBuffer();
                }) // 前后端分离开发的处理
                .sessionRegistry(sessionRegistry())  // 将 session 共享到 redis 中
                .maxSessionsPreventsLogin(true); // 登录之后静止再次登录

        return httpSecurity.build();
    }

    /**
     * 创建 session 同步到 redis 的方案
     * @return
     */
    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(findByIndexNameSessionRepository);
    }
}
