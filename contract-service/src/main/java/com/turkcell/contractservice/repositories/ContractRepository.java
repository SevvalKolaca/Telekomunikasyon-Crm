package com.turkcell.contractservice.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.contractservice.entities.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    boolean existsByContractNumber(String contractNumber);

    List<Contract> findByCustomerId(String customerId);
}