package com.turkcell.billing_payment_service.service;

import com.turkcell.billing_payment_service.entity.Bill;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillService {
    Bill save(Bill bill);
    List<Bill> findAll();
    Optional<Bill> findById(Long id);
    List<Bill> findByCustomerId(Long customerId);
    List<Bill> findByStatus(String status);
    List<Bill> findByDueDateBefore(LocalDate date);
    void deleteById(Long id);
} 