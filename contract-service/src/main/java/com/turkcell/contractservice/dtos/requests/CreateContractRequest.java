package com.turkcell.contractservice.dtos.requests;

import com.turkcell.contractservice.entities.enums.BillingPlan;
import java.time.LocalDateTime;

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
    
    @NotBlank(message = "Müşteri ID boş olamaz")
    private String customerId;
    
    @NotNull(message = "Faturalama planı boş olamaz")
    private BillingPlan billingPlan;
} 