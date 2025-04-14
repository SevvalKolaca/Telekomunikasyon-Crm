package com.turkcell.billing_payment_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.turkcell.billing_payment_service.entity.Contract;

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

    public ContractResponseDTO(Contract contract) {
        this.id = contract.getId();
        this.contractNumber = contract.getContractNumber();
        this.startDate = contract.getStartDate();
        this.endDate = contract.getEndDate();
        this.customerId = contract.getCustomerId();
        this.planId = contract.getPlanId();
        this.billingPeriod = contract.getBillingPeriod();
        this.status = contract.getStatus();
        this.isActive = contract.getIsActive();
        this.price = contract.getPrice();
        this.cancellationReason = contract.getCancellationReason();
        this.cancellationDate = contract.getCancellationDate();
        this.lastUpdateDate = contract.getLastUpdateDate();
        this.createdAt = contract.getCreatedAt();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getPlanId() {
        return planId;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Double getPrice() {
        return price;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
} 