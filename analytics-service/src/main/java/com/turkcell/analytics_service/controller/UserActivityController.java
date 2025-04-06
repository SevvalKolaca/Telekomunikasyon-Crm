package com.turkcell.analytics_service.controller;

import com.turkcell.analytics_service.dto.UserActivityDto;
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
    public ResponseEntity<Void> logUserActivity(@RequestBody UserActivityDto activityDto) {
        try {
            userActivityService.logActivity(activityDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
