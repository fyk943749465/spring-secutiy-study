package com.feng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 内存中自定义数据源
    @Bean
    public UserDetailsService userDetailsService() {

        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("ADMIN").build());
        inMemoryUserDetailsManager.createUser(User.withUsername("win7").password("{noop}123").roles("USER").build());
        inMemoryUserDetailsManager.createUser(User.withUsername("lisi").password("{noop}123").authorities("READ_INFO").build());
        return inMemoryUserDetailsManager;
    }




    // 定义全局的 authenticationManager
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .mvcMatchers("/admin").hasRole("ADMIN")  // 访问 /admin 时，必须具有 ADMIN角色
                .mvcMatchers("/user").hasRole("USER")
                .mvcMatchers("/getInfo").hasAuthority("READ_INFO") // 访问 /getInfo 时必须具有 READ_INFO 权限
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .csrf().disable();

        return httpSecurity.build();
    }
}
