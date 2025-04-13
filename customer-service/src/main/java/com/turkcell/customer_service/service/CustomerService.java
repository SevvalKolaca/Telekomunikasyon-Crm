package com.turkcell.customer_service.service;

import com.turkcell.customer_service.dto.CustomerRequest;
import com.turkcell.customer_service.dto.CustomerResponse;
import com.turkcell.customer_service.dto.Contract.GetContractResponse;
import com.turkcell.customer_service.enums.BillingPeriod;

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
    void add(String customerId, UUID planId, BillingPeriod billingPeriod, LocalDateTime startDate, LocalDateTime endDate);
    void update(UUID contractId, LocalDateTime endDate, BillingPeriod billingPeriod);
    void cancelContract(UUID contractId, String reason);
    void suspendContract(UUID contractId);
    void reactivateContract(UUID contractId);
}
