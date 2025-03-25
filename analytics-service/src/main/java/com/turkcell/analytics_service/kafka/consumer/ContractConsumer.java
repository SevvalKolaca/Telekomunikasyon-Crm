package com.turkcell.analytics_service.kafka.consumer;

import org.springframework.stereotype.Component;
import com.turkcell.analytics_service.service.AnalyticsService;
import io.github.ergulberke.event.contract.ContractCreatedEvent;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;

@Component
public class ContractConsumer {
    private final AnalyticsService analyticsService;

    public ContractConsumer(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Bean
    public Consumer<ContractCreatedEvent> contractCreatedFunction() {
        return event -> {
            System.out.println("Contract event received: " + event.getContractId());
            analyticsService.saveContractAnalytics(event);
        };
    }
}
