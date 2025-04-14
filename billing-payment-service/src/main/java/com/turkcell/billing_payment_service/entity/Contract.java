package com.turkcell.billing_payment_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    // Setters
    public void setId(UUID id) {
        this.id = id;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public void setPlanId(UUID planId) {
        this.planId = planId;
    }

    public void setBillingPeriod(String billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
