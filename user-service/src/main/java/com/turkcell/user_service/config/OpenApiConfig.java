package com.turkcell.user_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .version("1.0")
                        .description("Telekomunikasyon CRM User Service API Documentation")
                        .contact(new Contact()
                                .name("Turkcell")
                                .email("info@turkcell.com.tr"))
                        .license(new License()
                                .name("Turkcell License")
                                .url("https://www.turkcell.com.tr")));
    }
}