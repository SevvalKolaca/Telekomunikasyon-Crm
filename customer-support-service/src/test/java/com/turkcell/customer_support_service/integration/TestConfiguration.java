package com.turkcell.customer_support_service.integration;

import com.turkcell.customer_support_service.entity.SupportTicket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    public Supplier<Message<SupportTicket>> ticketSupplier() {
        return () -> {
            SupportTicket ticket = new SupportTicket();
            ticket.setSubject("Test Ticket");
            return MessageBuilder.withPayload(ticket).build();
        };
    }

    @Bean
    @Primary
    public Consumer<Message<SupportTicket>> ticketFunction() {
        return message -> {
            // Test Consumer - sadece mesajı tüketir
        };
    }

    @Bean
    @Primary
    public Consumer<Message<String>> ticketStatusFunction() {
        return message -> {
            // Test Consumer - sadece mesajı tüketir
        };
    }
}