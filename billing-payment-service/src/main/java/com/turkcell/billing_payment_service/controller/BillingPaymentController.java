package com.turkcell.billing_payment_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/billingpayment")
public class BillingPaymentController {

    @GetMapping
    public String get(){
        return "Billing Payment Service Calisiyor.";
    }

}
