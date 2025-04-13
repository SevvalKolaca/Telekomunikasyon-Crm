package com.turkcell.contractservice.dtos.requests;

import com.turkcell.contractservice.entities.enums.BillingPeriod;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateContractRequest {
    @NotBlank(message = "Sözleşme numarası boş olamaz")
    private String contractNumber;
    
    @NotNull(message = "Başlangıç tarihi boş olamaz")
    private LocalDateTime startDate;
    
    @NotNull(message = "Bitiş tarihi boş olamaz")
    private LocalDateTime endDate;
    
    @NotNull(message = "Müşteri ID boş olamaz")
    private UUID customerId;

    @NotNull(message = "Plan ID boş olamaz")
    private UUID planId;
    
    @NotNull(message = "Faturalama periyodu boş olamaz")
    private BillingPeriod billingPeriod;

    @NotNull(message = "Fatura tutarı boş olamaz")
    private Double price;
} 