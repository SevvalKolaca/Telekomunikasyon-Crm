package com.turkcell.analytics_service.config;

import com.turkcell.analytics_service.event.SubscriptionCreatedEvent;

import io.github.ergulberke.event.user.UserCreatedEvent;

import com.turkcell.analytics_service.event.BillingCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.logging.Logger;

@Configuration
public class EventConsumerConfig {

    private static final Logger logger = Logger.getLogger(EventConsumerConfig.class.getName());

    @Bean
    public Consumer<UserCreatedEvent> userCreatedFunction() {
        return event -> {
            logger.info("Yeni kullanıcı oluşturuldu: " + event.getUsername() +
                    " (ID: " + event.getUserId() + ")");
        };
    }

    @Bean
    public Consumer<SubscriptionCreatedEvent> subscriptionCreatedFunction() {
        return event -> {
            logger.info("Yeni abonelik oluşturuldu: " + event.getSubscriptionId() +
                    " (Plan: " + event.getPlanName() + ", Fiyat: " + event.getPrice() +
                    " " + event.getCurrency() + ")");
        };
    }

    @Bean
    public Consumer<BillingCreatedEvent> billingCreatedFunction() {
        return event -> {
            logger.info("Yeni fatura oluşturuldu: " + event.getInvoiceId() +
                    " (Tutar: " + event.getAmount() + " " + event.getCurrency() +
                    ", Vade: " + event.getDueDate() + ")");
        };
    }
}