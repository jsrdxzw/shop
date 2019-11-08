package com.jsrdxzw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/05
 * @Description:
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    /**
     * 配置swagger2核心配置
     * swagger-ui.html
     * doc.html
     *
     * @return Docket
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(createApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jsrdxzw.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                .title("电商平台接口api")
                .contact(new Contact("jsrdxzw", "https://www.jsrdxzw.com", "jsrdxzw@163.com"))
                .description("专为吃货提供的api文档")
                .version("1.0.0")
                .termsOfServiceUrl("https://www.jsrdxzw.com")
                .build();
    }
}
