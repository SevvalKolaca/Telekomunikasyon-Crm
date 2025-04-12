package com.turkcell.user_service.config;

import com.turkcell.user_service.security.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Profile("!test")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // actuator endpoint'leri herkes tarafından erişilebilir
                        .requestMatchers("/actuator/**").permitAll()
                        // public endpoints önce yazılmalı !!!
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()  // LOGIN sayfasını açık bırakıyoruz
                        .requestMatchers("/error").permitAll()
                        // CUSTOMER_SERVICE için yetkilendirme
                        //.requestMatchers("/customer-help/**").hasAuthority("ROLE_CUSTOMER_SERVICE")
                        // TECH_SUPPORT için yetkilendirme
                        //.requestMatchers("/tech-help/**").hasAuthority("ROLE_TECH_SUPPORT")

                        // Test
                        //.requestMatchers("/users/**").permitAll()  // LOGIN sayfasını açık bırakıyoruz

                        // Kullanıcı işlemleri -- GET, PUT, POST, DELETE
                        .requestMatchers(HttpMethod.GET, "/users/getAll").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET,"/users/get-user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER_SERVICE", "ROLE_TECH_SUPPORT")
                        .requestMatchers(HttpMethod.POST,"/users/create").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/users/update-user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER_SERVICE","ROLE_TECH_SUPPORT")
                        .anyRequest().hasAuthority("ROLE_ADMIN")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}