package com.feng.springsecrity02.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring security 相关配置
 */
@Configuration
public class SecurityConfigure {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers("/login.html").permitAll() // 放行 login.html
                .anyRequest().authenticated() // 所有请求都开启认证
                .and()
                .formLogin()  // 开启表单认证
                .loginPage("/login.html") // 表单登录页面是 login.html
                .loginProcessingUrl("/doLogin")
                .usernameParameter("uname")  // 修改默认的表单提交参数用户名的名称，默认的必须是 username
                .passwordParameter("passwd")    // 修改默认的表单提交参数密码的名称，默认的是 password
                .defaultSuccessUrl("/index.html",true)
                .failureUrl("/login.html") // 登录失败重定向到登录页面
                .and()
                .logout() // 开启退出登录
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")  // 退出成功后，回到登录页面
                .and()
                .csrf().disable() // 漏洞保护关闭
        ;

        return http.build();
    }
}
