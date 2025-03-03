package com.turkcell.billing_payment_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

import org.hibernate.validator.constraints.UUID;

@Entity
@Getter
@Setter
public class Bill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID billNumber;

    private Double amount;

    private LocalDate dueDate;

    private LocalDate paymentDate;

    private String status;

    private Long customerId;

}
