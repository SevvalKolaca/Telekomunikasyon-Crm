package com.turkcell.customer_service.service;

import com.turkcell.customer_service.dto.CustomerRequest;
import com.turkcell.customer_service.dto.CustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse getCustomerByEmail(String email);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse updateCustomer(UUID id,CustomerRequest request);
    void deleteCustomer(UUID id);
}
