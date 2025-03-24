package com.turkcell.analytics_service.kafka.consumer;

import org.springframework.stereotype.Component;

import com.turkcell.analytics_service.service.AnalyticsService;

import io.github.ergulberke.event.plan.PlanCreatedEvent;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@Component
public class PlanConsumer {

    @Autowired
    private AnalyticsService analyticsService;

    @Bean
    public Consumer<PlanCreatedEvent> planCreatedFunction() {
        return event -> {
            System.out.println("Kafka'dan gelen Plan Created Event: " + event);
            // Veriyi kaydetme işlemi
            analyticsService.savePlanAnalytics(event);
        };
    }
}
