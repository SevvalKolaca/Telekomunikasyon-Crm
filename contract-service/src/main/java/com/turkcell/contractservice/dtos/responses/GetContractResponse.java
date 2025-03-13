package com.turkcell.contractservice.dtos.responses;

import com.turkcell.contractservice.entities.enums.BillingPlan;
import com.turkcell.contractservice.entities.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetContractResponse {
    private UUID id;
    private String contractNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String customerId;
    private BillingPlan billingPlan;
    private ContractStatus status;
    private boolean isActive;
    private double monthlyFee;
    private String cancellationReason;
    private LocalDateTime cancellationDate;
    private LocalDateTime lastUpdateDate;
    private LocalDateTime createdAt;
} 