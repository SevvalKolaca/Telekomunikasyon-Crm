package com.turkcell.customer_service.service;

import com.turkcell.customer_service.dto.CustomerRequest;
import com.turkcell.customer_service.dto.CustomerResponse;
import com.turkcell.customer_service.entity.Customer;
import com.turkcell.customer_service.repository.CustomerRepository;
import com.turkcell.customer_service.rules.CustomerBusinnessRules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;
    private final CustomerBusinnessRules rules;


    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        rules.checkIfEmailExists(request.getEmail());
        rules.validatePhoneNumberIfPresent(request.getPhone());

        Customer customer = buildCustomerFromRequest(request);
        Customer savedCustomer = customerRepository.save(customer);
        return buildCustomerResponse(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        return null;
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return List.of();
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {
        return null;
    }

    @Override
    public void deleteCustomer(UUID id) {

    }

    private Customer buildCustomerFromRequest(CustomerRequest request) {
        return Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
    }

    private CustomerResponse buildCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
