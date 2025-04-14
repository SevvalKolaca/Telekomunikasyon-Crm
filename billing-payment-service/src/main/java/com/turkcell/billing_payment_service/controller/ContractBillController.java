package com.turkcell.billing_payment_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/v1/contract-bills")
public class ContractBillController {
    private final RestClient restClient;

    public ContractBillController(RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder.baseUrl("http://localhost:8083").build();
    }

    @GetMapping
    public String get() {
        String response = restClient.get().uri("/api/v1/contracts").retrieve().body(String.class);

        System.out.println("Contract'dan gelen response: " + response);
        return "Contract - Service";



    }   
}
    
