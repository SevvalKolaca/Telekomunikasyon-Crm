package com.turkcell.plan_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import com.turkcell.plan_service.service.PlanProducer;
import io.github.ergulberke.event.plan.PlanCreatedEvent;

@Service
public class PlanProducerImpl implements PlanProducer {

    @Autowired
    private StreamBridge streamBridge;

    @Override
    public void sendPlanCreatedEvent(PlanCreatedEvent event) {
        boolean result = streamBridge.send("planCreatedFunction-out-0", event);
        if (result) {
            System.out.println("Plan Created Event Kafka'ya gönderildi: " + event);
        } else {
            System.err.println("Plan Created Event Kafka'ya gönderilemedi!");
        }
    }
}
