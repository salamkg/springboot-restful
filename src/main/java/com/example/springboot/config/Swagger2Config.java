//package com.example.springboot.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Tag;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@EnableSwagger2
//@Configuration
//public class Swagger2Config {
//
//    public static final String Board = "Доска для задач";
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2).select()
//                .apis(RequestHandlerSelectors
//                        .basePackage("com.example.springboot.controllers"))
//                .paths(PathSelectors.any())
//                .build()
//                .pathMapping("/api")
//                .apiInfo(apiEndPointsInfo())
//                .tags(new Tag(Board, ""))
//                ;
//    }
//
//    private ApiInfo apiEndPointsInfo() {
//        return new ApiInfoBuilder().title("Board")
//                .description("Task Management API")
//                .version("1.0.0")
//                .build();
//    }
//
//}
