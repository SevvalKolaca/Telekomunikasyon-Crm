package com.turkcell.customer_service.dto.Plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanResponse {
    private UUID id;
    private String name;
    private String description;
    private String planType;
    private BigDecimal price;
    private Integer durationInMonths;
    private Boolean isActive;
}