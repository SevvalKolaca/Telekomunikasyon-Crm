package com.turkcell.customer_service.dto;

import com.turkcell.customer_service.enums.AccountStatus;
import com.turkcell.customer_service.validation.TurkishPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    //private AccountStatus accountStatus;
}
