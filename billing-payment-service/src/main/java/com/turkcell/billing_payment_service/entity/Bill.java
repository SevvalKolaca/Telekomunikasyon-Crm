package com.turkcell.billing_payment_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Bill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="bill_number",nullable = false)
    private String billNumber;


    @Column(name="amount",nullable = false)
    private Double amount;

    @Column(name="due_date",nullable = false)
    private LocalDate dueDate;

    @Column(name="payment_date")
    private LocalDate paymentDate;

    @Column(name="status",nullable = false)
    private String status;

    @Column(name="customer_id",nullable = false)
    private Long customerId;

}
