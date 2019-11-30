package com.jsrdxzw.config;

import com.jsrdxzw.resource.FileProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/26
 * @Description: 要实现静态资源处理
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileProperties fileProperties;

    public WebMvcConfig(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // file:/Users/...
        // 映射swagger2
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("file:" + fileProperties.getImageUserFaceLocation());
    }
}
