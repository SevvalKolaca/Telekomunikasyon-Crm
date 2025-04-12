package com.turkcell.customer_support_service.config;

// JWT için gerekli olan bean'leri burada tanımlıyoruz
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.ergulberke.jwt.JwtTokenProvider;

@Configuration
public class AppConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }
}
