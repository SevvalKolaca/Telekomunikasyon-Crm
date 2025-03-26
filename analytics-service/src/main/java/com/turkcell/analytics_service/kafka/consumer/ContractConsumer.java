package com.turkcell.analytics_service.kafka.consumer;

import org.springframework.stereotype.Component;
import com.turkcell.analytics_service.service.SubscriptionAnalyticsService;
import io.github.ergulberke.event.contract.ContractCreatedEvent;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j // Loglama için
public class ContractConsumer {

    private final SubscriptionAnalyticsService subscriptionAnalyticsService;

    public ContractConsumer(SubscriptionAnalyticsService subscriptionAnalyticsService) {
        this.subscriptionAnalyticsService = subscriptionAnalyticsService;
    }

    @Bean
    public Consumer<ContractCreatedEvent> contractCreatedFunction() {
        return event -> {
            try {
                log.info("📩 Contract event received: {}", event.getContractId());
                subscriptionAnalyticsService.saveSubscriptionAnalytics(event);
                log.info("✅ Contract event successfully processed: {}", event.getContractId());
            } catch (Exception e) {
                log.error("❌ Contract event processing failed: {}", event.getContractId(), e);
            }
        };
    }
}
