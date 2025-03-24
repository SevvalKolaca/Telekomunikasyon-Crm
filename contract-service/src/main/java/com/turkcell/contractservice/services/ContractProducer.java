package com.turkcell.contractservice.services;

import io.github.ergulberke.event.contract.ContractCreatedEvent;

public interface ContractProducer {
    void sendContractCreatedEvent(ContractCreatedEvent event);
}
