package com.turkcell.planservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "contract-service")
public interface ContractClient {

    @GetMapping("/api/contracts/plan/{planId}")
    ResponseEntity<List<ContractResponse>> getContractsByPlanId(@PathVariable("planId") UUID planId);
    
    @PostMapping("/api/contracts/verify-plan")
    ResponseEntity<Boolean> verifyPlanAvailability(@RequestBody PlanVerificationRequest request);
} 