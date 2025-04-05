package com.turkcell.customer_service.dto;

import com.turkcell.customer_service.enums.AccountStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class CustomerResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AccountStatus accountStatus;
    private UUID planId;
    private String planName;
    private LocalDate planStartDate;
    private LocalDate planEndDate;
}
