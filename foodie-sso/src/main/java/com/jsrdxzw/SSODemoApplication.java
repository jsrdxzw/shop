package com.jsrdxzw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.jsrdxzw.mapper"})
@ComponentScan(basePackages = {"com.jsrdxzw", "org.n3r.idworker"})
public class SSODemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SSODemoApplication.class, args);
    }
}
