package com.turkcell.user_service.security;

import io.github.ergulberke.model.Role;
import io.github.ergulberke.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String token = request.getHeader("Authorization");
        System.out.println("===== JWT FILTER START =====");
        System.out.println("Authorization Header: " + token);

        // Eğer header boş değilse, boşluk karakterlerine göre böl ve son parçayı token olarak al
        if (token != null && token.contains(" ")) {
            String[] parts = token.split(" ");
            token = parts[parts.length - 1];
            System.out.println("Normalized Token: " + token);
        }

        if (token != null && !token.isBlank()) {
            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token);
                Role role = jwtTokenProvider.getRoleFromToken(token);
                System.out.println("Token doğrulandı! Username: " + username + ", Rol: " + role);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.authority());
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(auth);

                System.out.println("SecurityContext Authentication: " + SecurityContextHolder.getContext().getAuthentication());
            } else {
                System.out.println("Token geçersiz!");
            }
        } else {
            System.out.println("Authorization header boş ya da hatalı!");
        }

        try {
            filterChain.doFilter(request, response);
        }
        catch (Exception ex) {
            System.out.println("JWT Filter içinde hata oluştu: " + ex.getMessage());
            ex.printStackTrace(); // terminalde detaylı hatayı görmek için

            // Eğer JWT ile ilgili hata varsa kullanıcıya doğrudan 403 dönebilirsin:
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT geçersiz veya işlenemedi.");
        }
        System.out.println("===== JWT FILTER END =====\n");

    }
}
