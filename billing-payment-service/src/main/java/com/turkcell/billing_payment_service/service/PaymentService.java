package com.turkcell.billing_payment_service.service;

import com.turkcell.billing_payment_service.dto.PaymentRequestDTO;
import com.turkcell.billing_payment_service.dto.PaymentResponseDTO;
import com.turkcell.billing_payment_service.entity.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment save(Payment payment);
    List<Payment> findAll();
    Optional<Payment> findById(Long id);
    List<Payment> findByCustomerId(Long customerId);
    List<Payment> findByStatus(String status);
    List<Payment> findByPaymentMethod(String paymentMethod);
    void deleteById(Long id);
    
    PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO getPaymentStatus(Long paymentId);
}
