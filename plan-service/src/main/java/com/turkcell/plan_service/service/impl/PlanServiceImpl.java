package com.turkcell.plan_service.service.impl;

import com.turkcell.plan_service.dto.CreatePlanRequest;
import com.turkcell.plan_service.dto.PlanDTO;
import com.turkcell.plan_service.dto.UpdatePlanRequest;
import com.turkcell.plan_service.entity.Plan;
import com.turkcell.plan_service.entity.PlanType;
import com.turkcell.plan_service.repository.PlanRepository;
import com.turkcell.plan_service.service.PlanService;
import com.turkcell.plan_service.validation.PlanValidationRules;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanValidationRules planValidationRules;

    @Override
    @Transactional
    public PlanDTO createPlan(CreatePlanRequest request) {
        planValidationRules.validatePlanCreation(request);
        
        Plan plan = new Plan();
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setPlanType(request.getPlanType());
        plan.setPrice(request.getPrice());
        plan.setDurationInMonths(request.getDurationInMonths());
        plan.setIsActive(true);

        return convertToDTO(planRepository.save(plan));
    }

    @Override
    @Transactional
    public PlanDTO updatePlan(UUID id, UpdatePlanRequest request) {
        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with id: " + id));

        planValidationRules.validatePlanUpdate(request);
        planValidationRules.validatePlanTypeChange(existingPlan, request.getPlanType());
        planValidationRules.validatePlanPriceChange(existingPlan, request.getPrice());
        planValidationRules.validatePlanDurationChange(existingPlan, request.getDurationInMonths());

        existingPlan.setName(request.getName());
        existingPlan.setDescription(request.getDescription());
        existingPlan.setIsActive(request.getIsActive());

        return convertToDTO(planRepository.save(existingPlan));
    }

    @Override
    public PlanDTO getPlanById(UUID id) {
        return planRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with id: " + id));
    }

    @Override
    public List<PlanDTO> getAllPlans() {
        return planRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanDTO> getPlansByType(PlanType planType) {
        return planRepository.findByPlanType(planType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanDTO> getActivePlans() {
        return planRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePlan(UUID id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with id: " + id));
        
        planValidationRules.validatePlanDeletion(plan);
        planRepository.deleteById(id);
    }

    private PlanDTO convertToDTO(Plan plan) {
        PlanDTO dto = new PlanDTO();
        dto.setId(plan.getId());
        dto.setName(plan.getName());
        dto.setDescription(plan.getDescription());
        dto.setPlanType(plan.getPlanType());
        dto.setPrice(plan.getPrice());
        dto.setDurationInMonths(plan.getDurationInMonths());
        dto.setIsActive(plan.getIsActive());
        return dto;
    }
} 