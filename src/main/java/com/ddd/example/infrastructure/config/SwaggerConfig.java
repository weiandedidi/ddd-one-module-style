package com.ddd.example.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * swagger2的配置，用于导入yapi
 * Yapi地址：https://yapi.ctcdn.cn/
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-03 14:50
 */
@EnableSwagger2WebMvc
@EnableOpenApi
@Configuration
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2) // 使用OpenAPI 3.0
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ddd.example.adapter.controller")) // 修改为你的基础包
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot REST API")
                .description("Spring Boot REST API with Swagger")
                .version("1.0.0")
                .build();
    }
}
