package com.turkcell.customer_service.dto.Contract;

import com.turkcell.customer_service.enums.BillingPlan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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