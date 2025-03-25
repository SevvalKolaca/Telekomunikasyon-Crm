package com.turkcell.analytics_service.controller;

import com.turkcell.analytics_service.dto.UserActivityDTO;
import com.turkcell.analytics_service.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

    private final UserActivityService userActivityService;

    // Yeni kullanıcı aktivitesi oluştur
    @PostMapping
    public ResponseEntity<UserActivityDTO> createUserActivity(@RequestBody UserActivityDTO dto) {
        return new ResponseEntity<>(userActivityService.createUserActivity(dto), HttpStatus.CREATED);
    }

    // Tüm kullanıcı aktivitelerini listele
    @GetMapping
    public ResponseEntity<List<UserActivityDTO>> getAllUserActivities() {
        return ResponseEntity.ok(userActivityService.getAllUserActivities());
    }

    // Kullanıcı ID'sine göre aktiviteleri getir
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserActivityDTO>> getUserActivitiesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(userActivityService.getUserActivitiesByUserId(userId));
    }

    // Belirli tarih aralığındaki aktiviteleri getir
    @GetMapping("/date-range")
    public ResponseEntity<List<UserActivityDTO>> getActivitiesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(userActivityService.getActivitiesByDateRange(startDate, endDate));
    }

    // Kullanıcı aktivitesini sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserActivity(@PathVariable UUID id) {
        userActivityService.deleteUserActivity(id);
        return ResponseEntity.noContent().build();
    }

    // Kullanıcı aktivitesini güncelle
    @PutMapping("/{id}")
    public ResponseEntity<UserActivityDTO> updateUserActivity(
            @PathVariable UUID id,
            @RequestBody UserActivityDTO dto) {
        return ResponseEntity.ok(userActivityService.updateUserActivity(id, dto));
    }
}
