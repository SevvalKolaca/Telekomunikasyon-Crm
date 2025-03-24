package com.turkcell.contractservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import io.github.ergulberke.event.contract.ContractCreatedEvent;

public class ContractProducerImpl {
    @Autowired
    private StreamBridge streamBridge;

    @Override
    public void sendContractCreatedEvent(ContractCreatedEvent event) {
        boolean result = streamBridge.send("contractCreatedFunction-out-0", event);
        if (result) {
            System.out.println("Contract Created Event Kafka'ya gönderildi: " + event);
        } else {
            System.err.println("Contract Created Event Kafka'ya gönderilemedi!");
        }
    }
}
