package com.turkcell.billing_payment_service.service.impl;

import com.turkcell.billing_payment_service.entity.Bill;
import com.turkcell.billing_payment_service.repository.BillRepository;
import com.turkcell.billing_payment_service.service.BillService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public Optional<Bill> findById(Long id) {
        return billRepository.findById(id);
    }

    @Override
    public List<Bill> findByCustomerId(Long customerId) {
        return billRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Bill> findByStatus(String status) {
        return billRepository.findByStatus(status);
    }

    @Override
    public List<Bill> findByDueDateBefore(LocalDate date) {
        return billRepository.findByDueDateBefore(date);
    }

    @Override
    public void deleteById(Long id) {
        billRepository.deleteById(id);
    }
} 