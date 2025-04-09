package com.turkcell.billing_payment_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.turkcell.billing_payment_service.service.BillProducer;

import io.github.ergulberke.event.billingPayment.BillCreatedEvent;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillProducerImpl implements BillProducer {

    @Autowired
    private StreamBridge streamBridge;
    private static final Logger log = LoggerFactory.getLogger(BillProducerImpl.class);


    @Override
    public void sendBillCreatedEvent(BillCreatedEvent event) {
        boolean result = streamBridge.send("billCreatedEvent-out-0", event);

        if (result) {
            log.info("✅ Bill Created Event Kafka'ya başarıyla gönderildi: {}", event);
        } else {
            log.error("❌ Bill Created Event Kafka'ya gönderilemedi!");
        }
    }
}
