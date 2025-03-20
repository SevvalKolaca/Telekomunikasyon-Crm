package com.turkcell.analytics_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.analytics_service.dto.SubscriptionAnalyticsDTO;
import com.turkcell.analytics_service.service.SubscriptionAnalyticsService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/subscription-analytics")
public class SubscriptionAnalyticsController {

    private final SubscriptionAnalyticsService subscriptionAnalyticsService;

    // Yeni abonelik analitiği oluştur
    @PostMapping
    public ResponseEntity<SubscriptionAnalyticsDTO> createSubscriptionAnalytics(
            @RequestBody SubscriptionAnalyticsDTO dto) {
        SubscriptionAnalyticsDTO created = service.createSubscriptionAnalytics(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

}
