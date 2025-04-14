package com.turkcell.billing_payment_service.service;

import org.springframework.stereotype.Service;
import com.turkcell.billing_payment_service.entity.Contract;
import com.turkcell.billing_payment_service.repository.ContractRepository;
import com.turkcell.billing_payment_service.dto.ContractRequestDTO;
import com.turkcell.billing_payment_service.dto.ContractResponseDTO;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public ContractResponseDTO createContract(ContractRequestDTO contractRequestDTO) {
        Contract contract = new Contract();
        contract.setContractNumber(contractRequestDTO.getContractNumber());
        contract.setStartDate(contractRequestDTO.getStartDate());
        contract.setEndDate(contractRequestDTO.getEndDate());
        contract.setCustomerId(contractRequestDTO.getCustomerId());
        contract.setPlanId(contractRequestDTO.getPlanId());
        contract.setBillingPeriod(contractRequestDTO.getBillingPeriod());
        contract.setStatus(contractRequestDTO.getStatus());
        contract.setIsActive(contractRequestDTO.getIsActive());
        contract.setPrice(contractRequestDTO.getPrice());
        contract.setCancellationReason(contractRequestDTO.getCancellationReason());
        contract.setCancellationDate(contractRequestDTO.getCancellationDate());
        contract.setLastUpdateDate(contractRequestDTO.getLastUpdateDate());
        contract.setCreatedAt(contractRequestDTO.getCreatedAt());
        contractRepository.save(contract);
        return new ContractResponseDTO(contract);
    }
    
    
    
    
}
