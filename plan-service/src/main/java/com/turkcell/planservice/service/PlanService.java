package com.turkcell.planservice.service;

import java.util.List;
import java.util.UUID;

import com.turkcell.planservice.dto.request.CreatePlanRequest;
import com.turkcell.planservice.dto.request.UpdatePlanRequest;
import com.turkcell.planservice.dto.response.PlanResponse;
import com.turkcell.planservice.enums.PlanStatus;
import com.turkcell.planservice.enums.PlanType;

public interface PlanService {
    
    List<PlanResponse> getAllPlans();
    
    PlanResponse getPlanById(UUID id);
    
    List<PlanResponse> getPlansByStatus(PlanStatus status);
    
    List<PlanResponse> getPlansByType(PlanType type);
    
    List<PlanResponse> getPlansByTypeAndStatus(PlanType type, PlanStatus status);
    
    PlanResponse createPlan(CreatePlanRequest request);
    
    PlanResponse updatePlan(UpdatePlanRequest request);
    
    void deletePlan(UUID id);

    boolean isPlanActive(UUID id);
} 