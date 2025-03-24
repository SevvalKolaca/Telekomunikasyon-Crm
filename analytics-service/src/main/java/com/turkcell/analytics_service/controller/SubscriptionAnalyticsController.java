package com.turkcell.analytics_service.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.turkcell.analytics_service.dto.SubscriptionAnalyticsDTO;
import com.turkcell.analytics_service.service.SubscriptionAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscription-analytics")
@RequiredArgsConstructor
public class SubscriptionAnalyticsController {

    private final SubscriptionAnalyticsService subscriptionAnalyticsService;

    // Abonelik analizini oluştur
    @PostMapping
    public ResponseEntity<SubscriptionAnalyticsDTO> createSubscriptionAnalytics(
            @RequestBody SubscriptionAnalyticsDTO dto) {
        return new ResponseEntity<>(subscriptionAnalyticsService.createSubscriptionAnalytics(dto), HttpStatus.CREATED);
    }

    // Abonelik analizini getir
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionAnalyticsDTO> getSubscriptionAnalyticsById(@PathVariable UUID id) {
        return ResponseEntity.ok(subscriptionAnalyticsService.getSubscriptionAnalyticsById(id));
    }

    // Plan ID'sine göre abonelik analizini getir
    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<SubscriptionAnalyticsDTO>> getSubscriptionAnalyticsByPlanId(@PathVariable UUID planId) {
        return ResponseEntity.ok(subscriptionAnalyticsService.getSubscriptionAnalyticsByPlanId(planId));
    }

    // Abonelik durumuna göre abonelik analizini getir
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SubscriptionAnalyticsDTO>> getSubscriptionAnalyticsByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(subscriptionAnalyticsService.getSubscriptionAnalyticsByStatus(status));
    }

    // Tüm abonelik analizlerini getir
    @GetMapping("/all")
    public ResponseEntity<List<SubscriptionAnalyticsDTO>> getAllSubscriptionAnalytics() {
        return ResponseEntity.ok(subscriptionAnalyticsService.getAllSubscriptionAnalytics());
    }

}
