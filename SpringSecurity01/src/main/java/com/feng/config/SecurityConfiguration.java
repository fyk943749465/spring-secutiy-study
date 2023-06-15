package com.feng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.util.AntPathMatcher;

@Configuration
public class SecurityConfiguration {


    // 内存中自定义数据源
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails userDetails = User.withUsername("aaa")
//                .password("{noop}123")
//                .roles("admin").build();
//        return new InMemoryUserDetailsManager(userDetails);
//    }

//     自定义全局的 AuthenticationManager
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//
//        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
//
//
//        return authenticationManager;
//    }


    // 定义全局的 authenticationManager
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        //authenticationManagerBuilder.authenticationProvider();
        //authenticationManagerBuilder.authenticationProvider();
        //authenticationManagerBuilder.userDetailsService();
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        return authenticationManagerBuilder.build();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers("/login.html").permitAll()  // 放行 /login.html 请求
                .requestMatchers("/index").permitAll() // 除index之外，所有请求都需要认证
                .anyRequest().authenticated()  // 所有请求都需要认证
                .and()
                .formLogin()  // 需要什么方式的认证呢？ 表单认证
                .loginPage("/login.html") // 用来指定默认的登录页面，如果不指定，访问受保护资源时，还是回到了spring security 提供的登录页面了
                // 自定义了登录页面，需要明确指定登录页面的提交的地址，这地址在controller中可以不存在，
                // 但是必须要根登录页面的表单提交地址一致。这样才能被spring security 提供的默认的 用户密码
                // UsernamePasswordAuthenticationFilter fitler处理
                .loginProcessingUrl("/doLogin")
                .usernameParameter("uname")  // 修改默认的表单提交参数用户名的名称，默认的必须是 username
                .passwordParameter("pwd")    // 修改默认的表单提交参数密码的名称，默认的是 password
                // successForwardUrl 和 defaultSuccessUrl 默认选择一个，还有一个区别：
                // successForwardUrl 不管你之前请求过何种资源，请求成功后，都先跳转到 /index
                // 但是defaultSuccessUrl则不同，如果受保护资源第一次请求，请求成功后会跳转到 /index，
                // 如果受保护资源现在不是第一次请求，请求成功后，则跳转到受保护资源
                //.successForwardUrl("/index") // 认证成功 forward 跳转（服务器内部）
                //.defaultSuccessUrl("/index", true)   // 认证成功 redirect 之后跳转（相当于浏览器又发起一次请求）
                .successHandler(new MyAuthenticationSuccessHandler()) // 认证成功时的处理
                //.failureForwardUrl("/login.html") // 认证失败后的 forward 跳转
                //.failureUrl("/login.html") // 认证失败之后 redirect 跳转
                .failureHandler(new MyAuthenticationFailureHandler())
                .and()
                .logout()
                //.logoutUrl("/logout")  // 注销登录的url，默认值是 logout 可以修改，默认的请求方式是GET 方式
                .logoutRequestMatcher(
                        new OrRequestMatcher(
                                new AntPathRequestMatcher("/aa", "GET"),
                                new AntPathRequestMatcher("/bb", "POST")
                        )
                )
                .invalidateHttpSession(true) // 是否当前的 session 失效，默认值就是 true
                .clearAuthentication(true)   // 是否清除认证标记，默认值是 true
                //.logoutSuccessUrl("/login.html") // 注销登录成功后跳转到登录页面
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
//                .and()
//                .authenticationManager(new Cu)
                .and()
                .csrf().disable(); // 禁止 csrf 跨站请求保护

        return http.build();
    }
}
