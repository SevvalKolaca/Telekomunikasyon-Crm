package com.turkcell.billing_payment_service.controller;

import com.turkcell.billing_payment_service.dto.PaymentRequestDTO;
import com.turkcell.billing_payment_service.dto.PaymentResponseDTO;
import com.turkcell.billing_payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/billingpayment")
public class BillingPaymentController {

    private final PaymentService paymentService;

    @Autowired
    public BillingPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public PaymentResponseDTO processPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        return paymentService.processPayment(paymentRequestDTO);
    }

    @GetMapping("/status/{paymentId}")
    public PaymentResponseDTO getPaymentStatus(@PathVariable Long paymentId) {
        return paymentService.getPaymentStatus(paymentId);
    }

}
