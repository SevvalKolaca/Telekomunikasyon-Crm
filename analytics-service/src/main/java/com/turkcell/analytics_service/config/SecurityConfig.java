package com.turkcell.analytics_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Test ortamı için CSRF'i devre dışı bırak
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // API endpointlerine erişime izin ver
                        .anyRequest().authenticated());

        return http.build();
    }
}