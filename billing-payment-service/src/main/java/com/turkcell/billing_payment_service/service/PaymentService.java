package com.turkcell.billing_payment_service.service;

import com.turkcell.billing_payment_service.dto.PaymentRequestDTO;
import com.turkcell.billing_payment_service.dto.PaymentResponseDTO;

public interface PaymentService {


    PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO);

    PaymentResponseDTO getPaymentStatus(Long paymentId);



}
