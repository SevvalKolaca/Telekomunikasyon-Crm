package com.turkcell.analytics_service.controller;

import com.turkcell.analytics_service.dto.SubscriptionAnalyticsDto;
import com.turkcell.analytics_service.entity.SubscriptionAnalytics;
import com.turkcell.analytics_service.service.SubscriptionAnalyticsService;
import io.github.ergulberke.event.billingPayment.BillCreatedEvent;
import io.github.ergulberke.event.contract.ContractCreatedEvent;
import io.github.ergulberke.event.plan.PlanCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscription-analytics")
@RequiredArgsConstructor
public class SubscriptionAnalyticsController {

    private final SubscriptionAnalyticsService subscriptionAnalyticsService;

    // 📩 Event tabanlı kayıtlar
    @PostMapping("/contract")
    public ResponseEntity<Void> logContractCreated(@RequestBody ContractCreatedEvent event) {
        subscriptionAnalyticsService.saveSubscriptionAnalytics(event);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/billing")
    public ResponseEntity<Void> logBillingEvent(@RequestBody BillCreatedEvent event) {
        subscriptionAnalyticsService.processBillingEvent(event);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/plan")
    public ResponseEntity<Void> logPlanCreated(@RequestBody PlanCreatedEvent event) {
        subscriptionAnalyticsService.savePlanAnalytics(event);
        return ResponseEntity.ok().build();
    }

    // 🔍 GET Endpoints — Analytics filtreleme

    // Plan ID ile filtreleme
    @GetMapping("/by-plan-id/{planId}")
    public ResponseEntity<List<SubscriptionAnalytics>> getByPlanId(@PathVariable UUID planId) {
        return ResponseEntity.ok(subscriptionAnalyticsService.getAnalyticsByPlanId(planId));
    }

    // Plan adı ile filtreleme
    @GetMapping("/by-plan-name")
    public ResponseEntity<List<SubscriptionAnalytics>> getByPlanName(@RequestParam String name) {
        return ResponseEntity.ok(subscriptionAnalyticsService.getAnalyticsByPlanName(name));
    }

    // Abonelik durumu (aktif/pasif vs.) ile filtreleme
    @GetMapping("/by-status")
    public ResponseEntity<List<SubscriptionAnalytics>> getByStatus(@RequestParam String status) {
        return ResponseEntity.ok(subscriptionAnalyticsService.getAnalyticsByStatus(status));
    }

    // Aylık/Yıllık filtreleme
    @GetMapping("/by-billing-cycle")
    public ResponseEntity<List<SubscriptionAnalytics>> getByBillingCycle(@RequestParam String cycle) {
        return ResponseEntity.ok(subscriptionAnalyticsService.getAnalyticsByBillingCycle(cycle));
    }
}
