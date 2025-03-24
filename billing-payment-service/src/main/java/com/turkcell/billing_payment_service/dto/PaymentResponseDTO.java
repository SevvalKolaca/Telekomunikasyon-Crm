package com.turkcell.billing_payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long paymentId;
    private String status;
    private LocalDateTime paymentDate;
}
