package com.jsrdxzw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/07
 * @Description:
 */
@Configuration
public class CorsConfig {

    private static final String ORIGIN_LOCATION = "http://localhost:8080";

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(ORIGIN_LOCATION);
        // 可发送cookie信息
        corsConfiguration.setAllowCredentials(true);
        // 允许请求的方式
        corsConfiguration.addAllowedMethod("*");
        // 允许的header
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();

        // 后端适用路径
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(corsConfigurationSource);
    }
}
