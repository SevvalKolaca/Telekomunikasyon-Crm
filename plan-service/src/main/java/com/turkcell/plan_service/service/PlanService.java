package com.turkcell.plan_service.service;

import com.turkcell.plan_service.dto.CreatePlanRequest;
import com.turkcell.plan_service.dto.PlanDTO;
import com.turkcell.plan_service.dto.UpdatePlanRequest;
import com.turkcell.plan_service.entity.PlanType;

import java.util.List;
import java.util.UUID;

public interface PlanService {
    PlanDTO createPlan(CreatePlanRequest request);
    PlanDTO updatePlan(UUID id, UpdatePlanRequest request);
    PlanDTO getPlanById(UUID id);
    List<PlanDTO> getAllPlans();
    List<PlanDTO> getPlansByType(PlanType planType);
    List<PlanDTO> getActivePlans();
    void deletePlan(UUID id);
} 