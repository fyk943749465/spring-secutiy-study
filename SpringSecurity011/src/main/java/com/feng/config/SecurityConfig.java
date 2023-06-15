package com.feng.config;

import com.feng.security.filter.KaptchaFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    KaptchaFilter kaptchaFilter(AuthenticationManager authenticationManager) {
        KaptchaFilter kaptchaFilter = new KaptchaFilter();
        kaptchaFilter.setFilterProcessesUrl("/doLogin");
        kaptchaFilter.setUsernameParameter("uname");
        kaptchaFilter.setPasswordParameter("password");
        kaptchaFilter.setKaptchaParameter("kaptcha");
        kaptchaFilter.setAuthenticationManager(authenticationManager);
        // 认证成功的处理
        kaptchaFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.sendRedirect("/index.html");  // 成功跳转到 /index.html页面
        });
        // 认证失败的处理
        kaptchaFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.sendRedirect("/login.html");
        });
        return kaptchaFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, KaptchaFilter kaptchaFilter) throws Exception {


        httpSecurity.authorizeHttpRequests()
                .mvcMatchers("/login.html").permitAll() // 放行这个请求
                .mvcMatchers("/vc.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")  // defaultLoginGeneratorFilter 的一个属性，不是 UsernamePasswordAuthenticationFilter

//                .defaultSuccessUrl("/index.html", true)
//                .failureUrl("/login.html")
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .csrf()
                .disable();
        // 覆盖原来的 UsernamePasswordAuthenticationFilter，使用自定义的 kaptchaFilter 进行认证
        httpSecurity.addFilterAt(kaptchaFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
