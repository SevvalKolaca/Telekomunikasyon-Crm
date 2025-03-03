package com.turkcell.billing_payment_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {

    @NotNull(message = "Amount cannot be null!")
    @Positive(message = "Amount must be greater than zero!")
    private Double amount;

    @NotNull(message = "Payment Method cannot be null!")
    private String paymentMethod;

    @NotNull(message = "Customer ID cannot be null!")
    private Long customerId;


    public double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
