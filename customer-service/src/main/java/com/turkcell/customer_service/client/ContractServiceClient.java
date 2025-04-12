package com.turkcell.customer_service.client;

import com.turkcell.customer_service.dto.Contract.CancelContractRequest;
import com.turkcell.customer_service.dto.Contract.CreateContractRequest;
import com.turkcell.customer_service.dto.Contract.UpdateContractRequest;
import com.turkcell.customer_service.dto.Contract.GetContractResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "contract-service", url = "${contract.service.url}")
public interface ContractServiceClient {
    
    @PostMapping("/api/contract")
    void add(@RequestBody CreateContractRequest request);
    
    @GetMapping("/api/contract/{contractId}")
    GetContractResponse getById(@PathVariable("contractId") UUID contractId);
    
    @GetMapping("/api/contract/customer/{customerId}")
    List<GetContractResponse> getByCustomerId(@PathVariable("customerId") String customerId);
    
    @PutMapping("/api/contract/{contractId}")
    void update(@PathVariable("contractId") UUID contractId, @RequestBody UpdateContractRequest request);
    
    @DeleteMapping("/api/contract/{contractId}")
    void delete(@PathVariable("contractId") UUID contractId);
    
    @PutMapping("/api/contract/{contractId}/cancel")
    void cancelContract(@PathVariable("contractId") UUID contractId, @RequestBody CancelContractRequest request);
    
    @PutMapping("/api/contract/{contractId}/suspend")
    void suspendContract(@PathVariable("contractId") UUID contractId);
    
    @PutMapping("/api/contract/{contractId}/reactivate")
    void reactivateContract(@PathVariable("contractId") UUID contractId);
} 