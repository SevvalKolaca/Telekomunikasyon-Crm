package com.turkcell.billing_payment_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {
    private Long paymentId;
    private String status;
    private LocalDateTime paymentDate;

    public PaymentResponseDTO(Long id, String status, LocalDateTime paymentDate) {

    }
}
