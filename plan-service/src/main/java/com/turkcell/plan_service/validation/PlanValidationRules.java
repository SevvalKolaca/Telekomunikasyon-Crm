package com.turkcell.plan_service.validation;

import com.turkcell.plan_service.dto.CreatePlanRequest;
import com.turkcell.plan_service.dto.UpdatePlanRequest;
import com.turkcell.plan_service.entity.Plan;
import com.turkcell.plan_service.entity.PlanType;
import com.turkcell.plan_service.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PlanValidationRules {

    public void validatePlanCreation(CreatePlanRequest request) {
        validatePlanName(request.getName());
        validatePlanDescription(request.getDescription());
        validatePlanPrice(request.getPrice());
        validatePlanDuration(request.getDurationInMonths());
    }

    public void validatePlanUpdate(UpdatePlanRequest request) {
        validatePlanName(request.getName());
        validatePlanDescription(request.getDescription());
        validatePlanPrice(request.getPrice());
        validatePlanDuration(request.getDurationInMonths());
        validateActiveStatus(request.getIsActive());
    }

    public void validatePlanDeletion(Plan plan) {
        if (!plan.getIsActive()) {
            throw new BusinessException("Cannot delete an inactive plan");
        }
    }

    public void validatePlanTypeChange(Plan existingPlan, PlanType newPlanType) {
        if (existingPlan.getPlanType() != newPlanType) {
            throw new BusinessException("Plan type cannot be changed");
        }
    }

    public void validatePlanPriceChange(Plan existingPlan, BigDecimal newPrice) {
        if (existingPlan.getPrice().compareTo(newPrice) != 0) {
            throw new BusinessException("Plan price cannot be changed");
        }
    }

    public void validatePlanDurationChange(Plan existingPlan, Integer newDuration) {
        if (!existingPlan.getDurationInMonths().equals(newDuration)) {
            throw new BusinessException("Plan duration cannot be changed");
        }
    }

    private void validatePlanName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("Plan name cannot be empty");
        }
        if (name.length() < 3 || name.length() > 100) {
            throw new BusinessException("Plan name must be between 3 and 100 characters");
        }
    }

    private void validatePlanDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new BusinessException("Plan description cannot be empty");
        }
        if (description.length() < 10 || description.length() > 500) {
            throw new BusinessException("Plan description must be between 10 and 500 characters");
        }
    }

    private void validatePlanPrice(BigDecimal price) {
        if (price == null) {
            throw new BusinessException("Plan price cannot be null");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Plan price must be greater than zero");
        }
    }

    private void validatePlanDuration(Integer duration) {
        if (duration == null) {
            throw new BusinessException("Plan duration cannot be null");
        }
        if (duration < 1 || duration > 24) {
            throw new BusinessException("Plan duration must be between 1 and 24 months");
        }
    }

    private void validateActiveStatus(Boolean isActive) {
        if (isActive == null) {
            throw new BusinessException("Active status cannot be null");
        }
    }
} 