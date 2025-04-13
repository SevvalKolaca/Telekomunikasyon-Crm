package com.turkcell.billing_payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractResponseDTO {
    private UUID id;
    private String contractNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID customerId;
    private UUID planId;
    private String billingPeriod;
    private String status;
    private Boolean isActive;
    private Double price;
    private String cancellationReason;
    private LocalDateTime cancellationDate;
    private LocalDateTime lastUpdateDate;
    private LocalDateTime createdAt;
}