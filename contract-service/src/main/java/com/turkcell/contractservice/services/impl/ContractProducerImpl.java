package com.turkcell.contractservice.services.impl;

import com.turkcell.contractservice.services.ContractProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import io.github.ergulberke.event.contract.ContractCreatedEvent;
import org.springframework.stereotype.Component;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ContractProducerImpl implements ContractProducer {
    @Autowired
    private StreamBridge streamBridge;
    
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private volatile boolean isShuttingDown = false;

    public void sendContractCreatedEvent(ContractCreatedEvent event) {
        if (isShuttingDown) {
            return;
        }
        
        executorService.submit(() -> {
            try {
                boolean result = streamBridge.send("contractCreatedFunction-out-0", event);
                if (result) {
                    System.out.println("Contract Created Event Kafka'ya gönderildi: " + event);
                } else {
                    System.err.println("Contract Created Event Kafka'ya gönderilemedi!");
                }
            } catch (Exception e) {
                System.err.println("Event gönderilirken hata oluştu: " + e.getMessage());
            }
        });
    }
    
    @PreDestroy
    public void shutdown() {
        isShuttingDown = true;
        executorService.shutdown();
    }
}
