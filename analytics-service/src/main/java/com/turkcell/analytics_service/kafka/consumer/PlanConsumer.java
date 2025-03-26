package com.turkcell.analytics_service.kafka.consumer;

import org.springframework.stereotype.Component;
import com.turkcell.analytics_service.service.SubscriptionAnalyticsService;
import io.github.ergulberke.event.plan.PlanCreatedEvent;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PlanConsumer {

    private final SubscriptionAnalyticsService subscriptionAnalyticsService;

    public PlanConsumer(SubscriptionAnalyticsService subscriptionAnalyticsService) {
        this.subscriptionAnalyticsService = subscriptionAnalyticsService;
    }

    @Bean
    public Consumer<PlanCreatedEvent> planCreatedFunction() {
        return event -> {
            try {
                log.info("📩 Plan Created Event received: {}", event.getPlanId());
                subscriptionAnalyticsService.savePlanAnalytics(event);
                log.info("✅ Plan event successfully processed: {}", event.getPlanId());
            } catch (Exception e) {
                log.error("❌ Plan event processing failed: {}", event.getPlanId(), e);
            }
        };
    }
}
