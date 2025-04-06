package com.turkcell.customer_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanChangeEvent {
    private UUID eventId;
    private String eventType;
    private LocalDateTime eventTime;
    private UUID customerId;
    private UUID oldPlanId;
    private UUID newPlanId;
    private String oldPlanName;
    private String newPlanName;
    private LocalDateTime changeDate;
    private String reason;
}