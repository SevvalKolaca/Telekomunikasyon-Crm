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
                        .requestMatchers("/v3/api-docs/**").permitAll() // Swagger API docs
                        .requestMatchers("/swagger-ui/**").permitAll() // Swagger UI
                        .requestMatchers("/swagger-ui.html").permitAll() // Swagger UI HTML
                        .requestMatchers("/swagger-resources/**").permitAll() // Swagger kaynakları
                        .requestMatchers("/actuator/**").permitAll() // Actuator endpoint'leri
                        .anyRequest().authenticated());

        return http.build();
    }
}