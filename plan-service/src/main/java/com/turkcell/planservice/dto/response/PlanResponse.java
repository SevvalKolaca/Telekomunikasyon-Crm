package com.turkcell.planservice.dto.response;

import com.turkcell.planservice.entity.PlanStatus;
import com.turkcell.planservice.entity.PlanType;
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
    private BigDecimal price;
    private PlanType type;
    private PlanStatus status;
    private Integer smsLimit;
    private Integer internetLimit;
    private Integer voiceLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 