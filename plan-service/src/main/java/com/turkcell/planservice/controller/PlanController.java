package com.turkcell.planservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.planservice.dto.request.CreatePlanRequest;
import com.turkcell.planservice.dto.request.UpdatePlanRequest;
import com.turkcell.planservice.dto.response.PlanResponse;
import com.turkcell.planservice.enums.PlanStatus;
import com.turkcell.planservice.enums.PlanType;
import com.turkcell.planservice.service.PlanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {
    
    private final PlanService planService;
    
    @GetMapping
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable UUID id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PlanResponse>> getPlansByStatus(@PathVariable PlanStatus status) {
        return ResponseEntity.ok(planService.getPlansByStatus(status));
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<PlanResponse>> getPlansByType(@PathVariable PlanType type) {
        return ResponseEntity.ok(planService.getPlansByType(type));
    }
    
    @GetMapping("/type/{type}/status/{status}")
    public ResponseEntity<List<PlanResponse>> getPlansByTypeAndStatus(
            @PathVariable PlanType type,
            @PathVariable PlanStatus status) {
        return ResponseEntity.ok(planService.getPlansByTypeAndStatus(type, status));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanResponse> createPlan(@Valid @RequestBody CreatePlanRequest request) {
        PlanResponse response = planService.createPlan(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanResponse> updatePlan(@Valid @RequestBody UpdatePlanRequest request) {
        return ResponseEntity.ok(planService.updatePlan(request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlan(@PathVariable UUID id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/active")
    public ResponseEntity<Boolean> isPlanActive(@PathVariable UUID id) {
        return ResponseEntity.ok(planService.isPlanActive(id));
    }
} 