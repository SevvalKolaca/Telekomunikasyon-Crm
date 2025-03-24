package com.turkcell.plan_service.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "customer-service", url = "${spring.services.customer.url}")
@RequestMapping("/api/v1/customers")
public interface CustomerServiceClient {
    
    @GetMapping("/{customerId}/has-active-contract")
    boolean hasActiveContract(@PathVariable("customerId") UUID customerId);
} 