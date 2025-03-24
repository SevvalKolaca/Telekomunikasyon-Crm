package com.turkcell.planservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "contract-service")
public interface ContractClient {

    @GetMapping("/api/contracts/plan/{planId}")
    ResponseEntity<List<ContractResponse>> getContractsByPlanId(@PathVariable("planId") Long planId);
    
    @PostMapping("/api/contracts/verify-plan")
    ResponseEntity<Boolean> verifyPlanAvailability(@RequestBody PlanVerificationRequest request);
}

class ContractResponse {
    private Long id;
    private Long customerId;
    private Long planId;
    private String status;
    
    // Getters, setters, constructors
}

class PlanVerificationRequest {
    private Long planId;
    private Long customerId;
    
    // Getters, setters, constructors
} 