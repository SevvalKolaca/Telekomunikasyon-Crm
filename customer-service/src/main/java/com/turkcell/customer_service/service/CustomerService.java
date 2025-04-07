package com.turkcell.customer_service.service;

import com.turkcell.customer_service.dto.CustomerRequest;
import com.turkcell.customer_service.dto.CustomerResponse;
import com.turkcell.contractservice.dtos.responses.GetContractResponse;
import com.turkcell.contractservice.entities.enums.BillingPlan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse getCustomerById(UUID id);
    CustomerResponse getCustomerByEmail(String email);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse updateCustomer(UUID id,CustomerRequest request);
    void deleteCustomer(UUID id);
    CustomerResponse changeCustomerPlan(UUID customerId, UUID newPlanId, String reason);
    
    // Sözleşme ile ilgili metodlar
    List<GetContractResponse> getByCustomerId(String customerId);
    GetContractResponse getById(UUID contractId);
    void add(String customerId, BillingPlan planId, LocalDateTime startDate, LocalDateTime endDate);
    void update(UUID contractId, LocalDateTime endDate, BillingPlan planId);
    void cancelContract(UUID contractId, String reason);
    void suspendContract(UUID contractId);
    void reactivateContract(UUID contractId);
}
