package com.turkcell.analytics_service.controller;

import com.turkcell.analytics_service.dto.UserActivityDTO;
import com.turkcell.analytics_service.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-activities")
@RequiredArgsConstructor
public class UserActivityController {

    private final UserActivityService service;

    // Yeni kullanıcı aktivitesi oluştur
    @PostMapping
    public ResponseEntity<UserActivityDTO> createUserActivity(@RequestBody UserActivityDTO dto) {
        UserActivityDTO created = service.createUserActivity(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Tüm kullanıcı aktivitelerini listele
    @GetMapping
    public ResponseEntity<List<UserActivityDTO>> getAllUserActivities() {
        List<UserActivityDTO> activities = service.getAllUserActivities();
        return ResponseEntity.ok(activities);
    }

    // Kullanıcı ID'sine göre aktiviteleri getir
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserActivityDTO>> getUserActivitiesByUserId(@PathVariable UUID userId) {
        List<UserActivityDTO> activities = service.getUserActivitiesByUserId(userId);
        return ResponseEntity.ok(activities);
    }

    // Belirli tarih aralığındaki aktiviteleri getir
    @GetMapping("/date-range")
    public ResponseEntity<List<UserActivityDTO>> getActivitiesByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(userActivityService.getActivitiesByDateRange(startDate, endDate));
    }

}
