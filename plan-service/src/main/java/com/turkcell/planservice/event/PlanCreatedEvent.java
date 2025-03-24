package com.turkcell.planservice.event;

import com.turkcell.planservice.entity.PlanStatus;
import com.turkcell.planservice.entity.PlanType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanCreatedEvent {
    private UUID planId;
    private String name;
    private BigDecimal price;
    private PlanType type;
    private PlanStatus status;
    private Integer smsLimit;
    private Integer internetLimit;
    private Integer voiceLimit;
    private LocalDateTime createdAt;
} 