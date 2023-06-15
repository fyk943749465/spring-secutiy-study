package com.feng.config;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.Arrays;
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
////    @Override
////    public void addCorsMappings(CorsRegistry registry) {
////        registry.addMapping("/**") // 处理的请求地址
////                .allowedMethods("*") // 所有方法
////                .allowedOrigins("*")
////                .allowedHeaders("*")
////                .allowCredentials(false)
////                .exposedHeaders("")
////                .maxAge(3600);
////    }
//
//
//    @Bean
//    FilterRegistrationBean<CorsFilter> corsFilter() {
//
//        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
//        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
//        corsConfiguration.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        registrationBean.setFilter(new CorsFilter(source));
//        registrationBean.setOrder(-1);
//        return registrationBean;
//    }
//}
