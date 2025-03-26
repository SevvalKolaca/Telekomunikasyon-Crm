package com.turkcell.planservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @GetMapping("/api/customers/{id}")
    ResponseEntity<CustomerResponse> getCustomerById(@PathVariable("id") UUID id);
}

class CustomerResponse {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    
    // Getters, setters, constructors
} 