package com.turkcell.plan_service.controller;

import com.turkcell.plan_service.dto.CreatePlanRequest;
import com.turkcell.plan_service.dto.PlanDTO;
import com.turkcell.plan_service.dto.UpdatePlanRequest;
import com.turkcell.plan_service.entity.PlanType;
import com.turkcell.plan_service.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plans")
//@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    public ResponseEntity<PlanDTO> createPlan(@Valid @RequestBody CreatePlanRequest request) {
        return new ResponseEntity<>(planService.createPlan(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanDTO> updatePlan(@PathVariable UUID id, @Valid @RequestBody UpdatePlanRequest request) {
        return ResponseEntity.ok(planService.updatePlan(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanDTO> getPlanById(@PathVariable UUID id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlanDTO>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping("/type/{planType}")
    public ResponseEntity<List<PlanDTO>> getPlansByType(@PathVariable PlanType planType) {
        return ResponseEntity.ok(planService.getPlansByType(planType));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PlanDTO>> getActivePlans() {
        return ResponseEntity.ok(planService.getActivePlans());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable UUID id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
} 