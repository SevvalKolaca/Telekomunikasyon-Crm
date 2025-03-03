package com.turkcell.billing_payment_service.repository;

import com.turkcell.billing_payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findByCustomerIdAndStatus(Long customerId, String status);

}
