package com.turkcell.user_service.controller;

import com.turkcell.user_service.dto.auth.LoginRequest;
import com.turkcell.user_service.entity.User;
import com.turkcell.user_service.repository.UserRepository;
import io.github.ergulberke.jwt.JwtTokenProvider;
import io.github.ergulberke.model.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthController(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("===== LOGIN İSTEĞİ GELDİ =====");
        System.out.println("Gelen email: " + loginRequest.getEmail());
        System.out.println("Gelen password: " + loginRequest.getPassword());

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            System.out.println("Kullanıcı bulunamadı: " + loginRequest.getEmail());
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        User user = userOptional.get();
        System.out.println("Bulunan Kullanıcı: " + user.getEmail() + " - Şifre: " + user.getPassword());

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            System.out.println("Şifreler uyuşmuyor!");
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        Role userRole = user.getRole();
        System.out.println("Kullanıcının Rolü: " + userRole);

        String token = jwtTokenProvider.createToken(user.getId(),user.getEmail(), userRole);
        System.out.println("Üretilen Token: " + token);
        System.out.println("===== LOGIN İSTEĞİ BİTTİ =====");

        return ResponseEntity.ok(token);
    }
}
