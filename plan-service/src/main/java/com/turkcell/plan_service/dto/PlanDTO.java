package com.turkcell.plan_service.dto;

import com.turkcell.plan_service.entity.PlanType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PlanDTO {
    private UUID id;
    private String name;
    private String description;
    private PlanType planType;
    private BigDecimal price;
    private Integer durationInMonths;
    private Boolean isActive;
} 