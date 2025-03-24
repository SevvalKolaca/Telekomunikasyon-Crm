package com.turkcell.analytics_service.kafka.consumer;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.turkcell.analytics_service.service.AnalyticsService;

import io.github.ergulberke.event.customer.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor

public class CustomerConsumer {
    @Autowired
    private AnalyticsService analyticsService;

    @Bean
    public Consumer<CustomerCreatedEvent> customerCreatedFunction() {
        return event -> {
            // Event'i AnalyticsService'e kaydediyoruz
            analyticsService.saveCustomerAnalytics(event);

            // Opsiyonel: Konsola yazdırma
            System.out.println("CustomerCreatedEvent alındı ve kaydedildi: " + event);
        };
    }
}
