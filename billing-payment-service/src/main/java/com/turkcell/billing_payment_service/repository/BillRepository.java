package com.turkcell.billing_payment_service.repository;

import com.turkcell.billing_payment_service.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByCustomerId(Long customerId);
    List<Bill> findByStatus(String status);
    List<Bill> findByDueDateBefore(LocalDate date);
} 