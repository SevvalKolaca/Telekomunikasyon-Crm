package com.turkcell.plan_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@FeignClient(name = "billing-service", url = "${spring.services.billing-payment.url}")
@RequestMapping("/api/v1/billing")
public interface BillingServiceClient {
    
    @GetMapping("/{customerId}/has-pending-payment")
    boolean hasPendingPayment(@PathVariable("customerId") UUID customerId);
} 