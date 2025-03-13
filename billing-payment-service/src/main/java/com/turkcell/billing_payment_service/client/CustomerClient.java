package com.turkcell.billing_payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.turkcell.billing_payment_service.entity.Customer;

// Customer servisiyle ileşim kurmak için
@FeignClient(name = "customer-service") // name ile çalışmazsa url de belirtebiliriz url = "http://localhost:8082" --> customer service portu
public interface CustomerClient {

    @GetMapping("/api/v1/customers/{id}")
    Customer getCustomerById(@PathVariable Long id);

    
}