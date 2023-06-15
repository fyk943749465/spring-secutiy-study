package com.example.springsecurityremembermesplit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails userDetails = User.withUsername("root")
                .password("{noop}123")
                .roles("admin").build();
        return new InMemoryUserDetailsManager(userDetails);
    }
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        return authenticationManagerBuilder.build();
    }
    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager,
                                   RememberMeServices rememberMeServices) {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setFilterProcessesUrl("/doLogin");
        loginFilter.setUsernameParameter("uname");
        loginFilter.setPasswordParameter("passwd");
        loginFilter.setAuthenticationManager(authenticationManager);
        // 设置认证成功时使用自定义的 rememberMeServices，第一次设置，是将cookie 写入客户端
        loginFilter.setRememberMeServices(rememberMeServices);

        // 认证成功处理
        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("msg", "登录成功");
            result.put("用户信息", authentication.getPrincipal());
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.OK.value());
            String s = new ObjectMapper().writeValueAsString(result);
            response.getWriter().println(s);
        });
        // 认证失败处理
        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("msg", "登录失败" + exception.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            String s = new ObjectMapper().writeValueAsString(result);
            response.getWriter().println(s);
        });
        return loginFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           LoginFilter loginFilter,
                                           RememberMeServices rememberMeServices) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .rememberMe() // 开启记住我的功能  基于 cookie 进行实现
                // 重定义 rememberMeService，因为默认的请求参数是从 request中获取的，而前后端分离的系统，需要从json中获取数据
                // 第二次使用 rememberMeServices，是session 过期后，自动登录，不需要再重新登录
                .rememberMeServices(rememberMeServices)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().println("请认证之后再去处理!");
                })
                .and()
                .logout()
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout", HttpMethod.DELETE.name()),
                        new AntPathRequestMatcher("/logout", HttpMethod.GET.name())
                ))
                .logoutSuccessHandler((request, response, authentication) -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("msg", "注销成功");
                    result.put("用户信息", authentication.getPrincipal());
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpStatus.OK.value());
                    String s = new ObjectMapper().writeValueAsString(result);
                    response.getWriter().println(s);
                })
                .and()
                .csrf()
                .disable();

        httpSecurity.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
         return new MyPersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(),
                 userDetailsService,
                 new InMemoryTokenRepositoryImpl());
//        return new PersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(),
//                 userDetailsService,
//                 new InMemoryTokenRepositoryImpl());
    }
}
