package com.turkcell.customer_service.client;

import com.turkcell.customer_service.dto.Plan.PlanResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "plan-service", url = "${plan.service.url}")
public interface PlanServiceClient {

    @GetMapping("/api/plans/{id}")
    ResponseEntity<PlanResponse> getPlanById(@PathVariable UUID id);

    @GetMapping("/api/plans/{id}/active")
    ResponseEntity<Boolean> isPlanActive(@PathVariable UUID id);
}