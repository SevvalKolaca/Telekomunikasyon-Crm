package com.turkcell.user_service.config;

import io.github.ergulberke.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// spring tarafından otomatik olarak tanınmadığı için manuel olarak ekledik.
@Configuration
public class JwtConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }
}
