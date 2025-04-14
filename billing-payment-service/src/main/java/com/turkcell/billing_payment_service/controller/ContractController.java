package com.turkcell.billing_payment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.billing_payment_service.service.ContractService;
import com.turkcell.billing_payment_service.dto.ContractRequestDTO;
import com.turkcell.billing_payment_service.dto.ContractResponseDTO;

@RestController
@RequestMapping("/api/contract")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    public ResponseEntity<ContractResponseDTO> createContract(@RequestBody ContractRequestDTO contractRequestDTO) {
        ContractResponseDTO contractResponseDTO = contractService.createContract(contractRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(contractResponseDTO);
    }
    
    
}
