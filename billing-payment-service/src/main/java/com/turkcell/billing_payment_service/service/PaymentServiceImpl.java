package com.turkcell.billing_payment_service.service;

import com.turkcell.billing_payment_service.dto.PaymentRequestDTO;
import com.turkcell.billing_payment_service.dto.PaymentResponseDTO;
import com.turkcell.billing_payment_service.entity.Payment;
import com.turkcell.billing_payment_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;


    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = new Payment();
        payment.setAmount(paymentRequestDTO.getAmount());
        payment.setPaymentMethod(paymentRequestDTO.getPaymentMethod());
        payment.setStatus("SUCCESS"); // Ödeme başarılı olduğunu varsayalım
        paymentRepository.save(payment);


        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setPaymentId(payment.getId());
        response.setStatus(payment.getStatus());
        response.setAmount(payment.getAmount());
        return response;
    }


    @Override
    public PaymentResponseDTO getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));


        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setPaymentId(payment.getId());
        response.setStatus(payment.getStatus());
        response.setAmount(payment.getAmount());
        return response;
    }
}
