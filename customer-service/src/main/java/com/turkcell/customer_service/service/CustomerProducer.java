package com.turkcell.customer_service.service;

import io.github.ergulberke.event.customer.CustomerCreatedEvent;

public interface CustomerProducer {
    void sendCustomerCreatedEvent(CustomerCreatedEvent event);
}
