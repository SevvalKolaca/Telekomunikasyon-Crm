package com.turkcell.analytics_service.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.turkcell.analytics_service.dto.SubscriptionAnalyticsDto;
import com.turkcell.analytics_service.service.SubscriptionAnalyticsService;
import io.github.ergulberke.event.contract.ContractCreatedEvent;
import io.github.ergulberke.event.billingPayment.BillCreatedEvent;
import io.github.ergulberke.event.plan.PlanCreatedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscription-analytics")
@RequiredArgsConstructor
public class SubscriptionAnalyticsController {

    private final SubscriptionAnalyticsService subscriptionAnalyticsService;

    // Sözleşme oluşturma olayını kaydet
    @PostMapping("/contract")
    public ResponseEntity<Void> logContractCreated(@RequestBody ContractCreatedEvent event) {
        subscriptionAnalyticsService.saveSubscriptionAnalytics(event);
        return ResponseEntity.ok().build();
    }

    // Fatura olayını işle
    @PostMapping("/billing")
    public ResponseEntity<Void> logBillingEvent(@RequestBody BillCreatedEvent event) {
        subscriptionAnalyticsService.processBillingEvent(event);
        return ResponseEntity.ok().build();
    }

    // Plan oluşturma olayını kaydet
    @PostMapping("/plan")
    public ResponseEntity<Void> logPlanCreated(@RequestBody PlanCreatedEvent event) {
        subscriptionAnalyticsService.savePlanAnalytics(event);
        return ResponseEntity.ok().build();
    }
}
