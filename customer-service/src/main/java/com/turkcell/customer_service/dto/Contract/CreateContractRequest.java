package com.turkcell.customer_service.dto.Contract;

import com.turkcell.customer_service.enums.BillingPeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

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
