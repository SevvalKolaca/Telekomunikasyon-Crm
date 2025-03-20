package com.turkcell.analytics_service.kafka.consumer;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.ergulberke.event.customer.CustomerCreatedEvent;

@Configuration
public class CustomerConsumer {

    @Bean
    public Consumer<CustomerCreatedEvent> customerCreatedFunction() {
        return event -> System.out
                .println(event.getCustomerId() + "  " + event.getFullName() + "  " + event.getEmail() + "  " +
                        event.getPhone() + "  " + event.getAddress() + "  " + event.getStatus() + "  "
                        + event.getEventType() + "  " + event.getTimestamp());
    }

}
