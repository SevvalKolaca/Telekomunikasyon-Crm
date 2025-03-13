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
import java.util.stream.Collectors;

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
    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = rules.findCustomerByIdOrThrow(id);
        return buildCustomerResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = rules.findCustomerByEmailOrThrow(email);
        return buildCustomerResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::buildCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {
        Customer existingCustomer = rules.findCustomerByIdOrThrow(id);

        rules.checkIfEmailExistsForUpdate(existingCustomer.getEmail(), request.getEmail());

        rules.validatePhoneNumberIfPresent(request.getPhone());

        existingCustomer.setFirstName(request.getFirstName());
        existingCustomer.setLastName(request.getLastName());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhone(request.getPhone());
        existingCustomer.setAddress(request.getAddress());
        existingCustomer.setAddress((request.getAddress()));
        //existingCustomer.getAccountStatus(request.getAccountStatus());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return buildCustomerResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(UUID id) {
        rules.checkIfCustomerExistsById(id);
        customerRepository.deleteById(id);
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
