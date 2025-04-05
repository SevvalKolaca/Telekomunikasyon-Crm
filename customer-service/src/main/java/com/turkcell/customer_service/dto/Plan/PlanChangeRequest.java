package com.turkcell.customer_service.dto.Plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanChangeRequest {
    private UUID customerId;
    private UUID oldPlanId;
    private UUID newPlanId;
    private LocalDate changeDate;
    private String reason;
}