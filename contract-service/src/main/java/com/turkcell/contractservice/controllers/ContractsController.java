package com.turkcell.contractservice.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.contractservice.dtos.requests.CancelContractRequest;
import com.turkcell.contractservice.dtos.requests.CreateContractRequest;
import com.turkcell.contractservice.dtos.requests.UpdateContractRequest;
import com.turkcell.contractservice.dtos.responses.GetContractResponse;
import com.turkcell.contractservice.entities.enums.ContractStatus;
import com.turkcell.contractservice.services.ContractService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/contract")
@AllArgsConstructor
public class ContractsController {
    private final ContractService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@Valid @RequestBody CreateContractRequest request) {
        service.add(request);
    }

    @GetMapping
    public List<GetContractResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GetContractResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<GetContractResponse> getByCustomerId(@PathVariable UUID customerId) {
        return service.getByCustomerId(customerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable UUID id, @Valid @RequestBody UpdateContractRequest request) {
        service.update(id, request);
    }

    @PutMapping("/{id}/status")
    public void updateStatus(@PathVariable UUID id, @RequestParam ContractStatus status) {
        service.updateStatus(id, status);
    }

    @PutMapping("/{id}/cancel")
    public void cancelContract(@PathVariable UUID id, @Valid @RequestBody CancelContractRequest request) {
        service.cancelContract(id, request);
    }

    @PutMapping("/{id}/suspend")
    public void suspendContract(@PathVariable UUID id) {
        service.suspendContract(id);
    }

    @PutMapping("/{id}/reactivate")
    public void reactivateContract(@PathVariable UUID id) {
        service.reactivateContract(id);
    }

} 