package com.turkcell.billing_payment_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.billing_payment_service.entity.Contract;

public interface ContractRepository extends JpaRepository<Contract, UUID> {
    
}
