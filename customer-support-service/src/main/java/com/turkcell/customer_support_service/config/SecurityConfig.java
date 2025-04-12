package com.turkcell.customer_support_service.config;

import io.github.ergulberke.jwt.JwtTokenProvider;
import jakarta.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Support ticket endpoints
                        .requestMatchers("/api/v1/support-tickets/**").hasAnyRole("SUPPORT_AGENT", "ADMIN")
                        .requestMatchers("/api/v1/support-tickets/{id}/assign").hasRole("SUPPORT_AGENT")
                        .requestMatchers("/api/v1/support-tickets/{id}/resolve").hasRole("SUPPORT_AGENT")
                        .requestMatchers("/api/v1/support-tickets/{id}/close").hasRole("SUPPORT_AGENT")

                        // Customer endpoints
                        .requestMatchers("/api/v1/customers/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/v1/customers/profile").hasRole("CUSTOMER")

                        // Admin endpoints
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")

                        // Deny all other requests
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
