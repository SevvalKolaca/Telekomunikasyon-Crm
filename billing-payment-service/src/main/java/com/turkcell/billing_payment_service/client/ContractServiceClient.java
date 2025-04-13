package com.turkcell.billing_payment_service.client;

import com.turkcell.billing_payment_service.dto.ContractResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "contract-service", url = "${contract.service.url}")
public interface ContractServiceClient {

    @GetMapping("/api/contract/{id}")
    ContractResponseDTO getContractById(@PathVariable UUID id);

    @GetMapping("/api/contract/customer/{customerId}")
    List<ContractResponseDTO> getContractsByCustomerId(@PathVariable UUID customerId);
}