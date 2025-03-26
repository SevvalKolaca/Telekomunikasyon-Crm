package com.turkcell.planservice.dto;

import io.github.ergulberke.enums.PlanStatus;
import io.github.ergulberke.enums.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private PlanType type;
    private PlanStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}