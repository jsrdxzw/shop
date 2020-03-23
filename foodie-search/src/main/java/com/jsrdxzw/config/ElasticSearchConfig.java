package com.jsrdxzw.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ElasticSearchConfig {
    @PostConstruct
    public void init() {
        // 解决和netty使用的bug
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
