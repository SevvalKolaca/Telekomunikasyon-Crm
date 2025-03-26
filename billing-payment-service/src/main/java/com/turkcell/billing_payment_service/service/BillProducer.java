package com.turkcell.billing_payment_service.service;

import io.github.ergulberke.event.billingPayment.BillCreatedEvent;

public interface BillProducer {
    void sendBillCreatedEvent(BillCreatedEvent event);
}
