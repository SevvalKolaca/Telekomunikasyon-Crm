package com.turkcell.analytics_service.kafka.consumer;

import org.springframework.stereotype.Component;
import com.turkcell.analytics_service.service.SubscriptionAnalyticsService;
import io.github.ergulberke.event.billingPayment.BillCreatedEvent;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BillingConsumer {

    private final SubscriptionAnalyticsService subscriptionAnalyticsService;

    public BillingConsumer(SubscriptionAnalyticsService subscriptionAnalyticsService) {
        this.subscriptionAnalyticsService = subscriptionAnalyticsService;
    }

    @Bean
    public Consumer<BillCreatedEvent> billCreatedFunction() {
        return event -> {
            try {
                log.info("📩 Bill Created Event received: Bill ID = {}, Customer ID = {}, Amount = {}",
                        event.getId(), event.getCustomerId(), event.getAmount());

                subscriptionAnalyticsService.processBillingEvent(event);

                log.info("✅ Bill event successfully processed: Bill ID = {}", event.getId());
            } catch (Exception e) {
                log.error("❌ Bill event processing failed: Bill ID = {}", event.getId(), e);
            }
        };
    }
}
