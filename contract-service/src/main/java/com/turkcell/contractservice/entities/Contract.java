package com.turkcell.contractservice.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.turkcell.contractservice.entities.enums.BillingPeriod;
import com.turkcell.contractservice.entities.enums.ContractStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contracts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;
    
    @Column(unique = true)
    private String contractNumber;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Column(name = "customer_id", nullable = false, columnDefinition = "uuid")
    private UUID customerId;

    @Column(name = "plan_id", nullable = false, columnDefinition = "uuid")
    private UUID planId;

    @Enumerated(EnumType.STRING)
    private BillingPeriod billingPeriod;
    
    @Enumerated(EnumType.STRING)
    private ContractStatus status;
    
    private Boolean isActive;
    private Double price;
    
    private String cancellationReason;
    private LocalDateTime cancellationDate;
    
    private LocalDateTime lastUpdateDate;
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdateDate = LocalDateTime.now();
        status = ContractStatus.ACTIVE;
        isActive = true;
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastUpdateDate = LocalDateTime.now();
    }
    
    public void cancel(String reason) {
        this.status = ContractStatus.TERMINATED;
        this.isActive = false;
        this.cancellationReason = reason;
        this.cancellationDate = LocalDateTime.now();
    }
    
    public void suspend() {
        this.status = ContractStatus.SUSPENDED;
        this.isActive = false;
    }
    
    public void reactivate() {
        this.status = ContractStatus.ACTIVE;
        this.isActive = true;
    }
    
    public void updateBillingPeriod(BillingPeriod newPeriod) {
        this.billingPeriod = newPeriod;
    }
} 