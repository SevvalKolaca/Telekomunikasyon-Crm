package com.turkcell.contractservice.services;

import java.util.List;
import java.util.UUID;

import com.turkcell.contractservice.dtos.requests.CancelContractRequest;
import com.turkcell.contractservice.dtos.requests.CreateContractRequest;
import com.turkcell.contractservice.dtos.requests.UpdateContractRequest;
import com.turkcell.contractservice.dtos.responses.GetContractResponse;
import com.turkcell.contractservice.entities.enums.ContractStatus;

public interface ContractService {
    void add(CreateContractRequest request);
    List<GetContractResponse> getAll();
    GetContractResponse getById(UUID id);
    void delete(UUID id);
    void update(UUID id, UpdateContractRequest request);
    List<GetContractResponse> getByCustomerId(String customerId);
    void updateStatus(UUID id, ContractStatus status);
    void cancelContract(UUID id, CancelContractRequest request);
    void suspendContract(UUID id);
    void reactivateContract(UUID id);
} 