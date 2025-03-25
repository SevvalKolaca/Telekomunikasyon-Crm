package com.turkcell.planservice.config;

import io.github.ergulberke.jwt.JwtTokenProvider;
import io.github.ergulberke.model.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        // Eğer header boş değilse, boşluk karakterlerine göre böl ve son parçayı token olarak al
        if (token != null && token.contains(" ")) {
            String[] parts = token.split(" ");
            token = parts[parts.length - 1];
        }

        if (token != null && !token.isBlank()) {
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    String username = jwtTokenProvider.getUsernameFromToken(token);
                    Role role = jwtTokenProvider.getRoleFromToken(token);

                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.authority());
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, Collections.singleton(authority));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                System.err.println("Token işleme hatası: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
} 