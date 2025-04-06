package com.turkcell.contractservice.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turkcell.contractservice.dtos.requests.CancelContractRequest;
import com.turkcell.contractservice.dtos.requests.CreateContractRequest;
import com.turkcell.contractservice.dtos.requests.UpdateContractRequest;
import com.turkcell.contractservice.dtos.responses.GetContractResponse;
import com.turkcell.contractservice.entities.Contract;
import com.turkcell.contractservice.entities.enums.ContractStatus;
import com.turkcell.contractservice.repositories.ContractRepository;
import com.turkcell.contractservice.services.ContractProducer;
import com.turkcell.contractservice.services.ContractService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class ContractManager implements ContractService {
    private final ContractRepository repository;
    private final ContractProducer contractProducer;


    @Override
    public void add(CreateContractRequest request) {
        if (repository.existsByContractNumber(request.getContractNumber()))
            throw new RuntimeException("Bu sözleşme numarası zaten mevcut.");

        Contract contract = new Contract();
        contract.setContractNumber(request.getContractNumber());
        contract.setStartDate(request.getStartDate());
        contract.setEndDate(request.getEndDate());
        contract.setCustomerId(request.getCustomerId());
        contract.setBillingPlan(request.getBillingPlan());
        contract.setMonthlyFee(request.getBillingPlan().calculateMonthlyFee());

         // Sözleşmeyi kaydediyoruz ve geri dönen nesneyi 'savedContract' olarak alıyoruz
        Contract savedContract = repository.save(contract);
        // Setter'ları kullanarak event'i oluşturuyoruz
        ContractCreatedEvent event = new ContractCreatedEvent();
        event.setContractId(savedContract.getId());
        event.setCustomerId(UUID.fromString(savedContract.getCustomerId())); // customerId'yi alıyoruz
        event.setPlanId(UUID.fromString(String.valueOf(savedContract.getBillingPlan().ordinal()))); // Enum'dan
                                                                                                    // planId'yi
                                                                                                    // alıyoruz
        event.setStartDate(savedContract.getStartDate());
        event.setEndDate(savedContract.getEndDate());
        event.setPrice(savedContract.getMonthlyFee());
        event.setCurrency("TRY"); // Eğer farklı bir para birimi eklemek isterseniz burada değiştirebilirsiniz
        event.setStatus(savedContract.getStatus().toString()); // Status'ü string olarak alıyoruz
        event.setEventType("CONTRACT_CREATED");
        event.setTimestamp(savedContract.getCreatedAt());

        // Producer ile event'i Kafka'ya gönderiyoruz
        contractProducer.sendContractCreatedEvent(event);
    }

    @Override
    public List<GetContractResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToGetContractResponse)
                .toList();
    }

    @Override
    public GetContractResponse getById(UUID id) {
        Contract contract = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sözleşme bulunamadı."));
        return mapToGetContractResponse(contract);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void update(UUID id, UpdateContractRequest request) {
        Contract contract = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sözleşme bulunamadı."));

        contract.setEndDate(request.getEndDate());
        contract.updateBillingPlan(request.getBillingPlan());

        repository.save(contract);
    }

    @Override
    public List<GetContractResponse> getByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToGetContractResponse)
                .toList();
    }

    @Override
    public void updateStatus(UUID id, ContractStatus status) {
        Contract contract = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sözleşme bulunamadı."));
        contract.setStatus(status);
        repository.save(contract);
    }

    @Override
    public void cancelContract(UUID id, CancelContractRequest request) {
        Contract contract = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sözleşme bulunamadı."));
        contract.cancel(request.getReason());
        repository.save(contract);
    }

    @Override
    public void suspendContract(UUID id) {
        Contract contract = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sözleşme bulunamadı."));
        contract.suspend();
        repository.save(contract);
    }

    @Override
    public void reactivateContract(UUID id) {
        Contract contract = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sözleşme bulunamadı."));
        contract.reactivate();
        repository.save(contract);
    }

    private GetContractResponse mapToGetContractResponse(Contract contract) {
        return new GetContractResponse(
                contract.getId(),
                contract.getContractNumber(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getCustomerId(),
                contract.getBillingPlan(),
                contract.getStatus(),
                contract.isActive(),
                contract.getMonthlyFee(),
                contract.getCancellationReason(),
                contract.getCancellationDate(),
                contract.getLastUpdateDate(),
                contract.getCreatedAt()
        );
    }
} 
