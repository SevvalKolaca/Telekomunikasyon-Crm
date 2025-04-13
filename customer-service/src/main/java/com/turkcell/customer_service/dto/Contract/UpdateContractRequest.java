package com.turkcell.customer_service.dto.Contract;

import com.turkcell.customer_service.enums.BillingPeriod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateContractRequest {
    @NotNull(message = "Bitiş tarihi boş olamaz")
    private LocalDateTime endDate;

    @NotNull(message = "Faturalama periyodu boş olamaz")
    private BillingPeriod billingPeriod;
}
