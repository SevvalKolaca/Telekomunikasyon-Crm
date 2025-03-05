package com.turkcell.billing_payment_service.service;

import com.turkcell.billing_payment_service.entity.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer save(Customer customer);
    List<Customer> findAll();
    Optional<Customer> findById(Long id);
    List<Customer> findByName(String name);
    Optional<Customer> findByEmail(String email);
    void deleteById(Long id);
} 