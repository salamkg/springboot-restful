package com.example.springboot.config;


import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Organization API")
                        .version("1.0")
                        .description("Documentation Organization API v1.0"));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("Task Management System")
                .packagesToScan("com.example.springboot.controllers")
                .pathsToMatch("/api/**")
                .build();
    }
}

