package com.jsrdxzw.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xuzhiwei
 */
@SpringBootApplication
@ComponentScan({"com.jsrdxzw.rabbitmq", "com.jsrdxzw.job"})
public class XxlJobExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(XxlJobExecutorApplication.class, args);
    }

}