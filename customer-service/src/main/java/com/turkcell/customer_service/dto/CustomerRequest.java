package com.turkcell.customer_service.dto;

import com.turkcell.customer_service.enums.AccountStatus;
import com.turkcell.contractservice.entities.enums.BillingPlan;
import com.turkcell.customer_service.validation.TurkishPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @TurkishPhoneNumber
    private String phone;

    private String address;

    @NotNull(message = "Plan ID cannot be null")
    private UUID planId;

    @NotBlank(message = "Plan name cannot be blank")
    private String planName;

    @NotNull(message = "Plan start date cannot be null")
    private LocalDate planStartDate;

    @NotNull(message = "Plan end date cannot be null")
    private LocalDate planEndDate;

    @NotNull(message = "Billing plan cannot be null")
    private BillingPlan billingPlan;

    //private AccountStatus accountStatus;
}
