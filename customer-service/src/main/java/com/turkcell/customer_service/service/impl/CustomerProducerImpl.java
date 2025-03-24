package com.turkcell.customer_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import com.turkcell.customer_service.service.CustomerProducer;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;

@Service
public class CustomerProducerImpl implements CustomerProducer {

    @Autowired
    private StreamBridge streamBridge;

    @Override
    public void sendCustomerCreatedEvent(CustomerCreatedEvent event) {
        boolean result = streamBridge.send("customerCreatedFunction-out-0", event);
        if (result) {
            System.out.println("Customer Created Event Kafka'ya gönderildi: " + event);
        } else {
            System.err.println("Customer Created Event Kafka'ya gönderilemedi!");
        }
    }
}
