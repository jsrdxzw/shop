package com.jsrdxzw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/03
 * @Description:
 */

@SpringBootApplication
@MapperScan(basePackages = {"com.jsrdxzw.mapper"})
@ComponentScan(basePackages = {"com.jsrdxzw", "org.n3r.idworker"})
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
