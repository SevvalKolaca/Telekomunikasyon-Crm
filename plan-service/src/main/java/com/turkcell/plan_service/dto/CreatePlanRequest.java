package com.turkcell.plan_service.dto;

import com.turkcell.plan_service.entity.PlanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePlanRequest {
    @NotBlank(message = "Plan name is required")
    private String name;

    @NotBlank(message = "Plan description is required")
    private String description;

    @NotNull(message = "Plan type is required")
    private PlanType planType;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationInMonths;
} 