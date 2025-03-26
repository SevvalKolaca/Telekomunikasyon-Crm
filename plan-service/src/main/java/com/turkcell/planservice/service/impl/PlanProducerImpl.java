package com.turkcell.planservice.service.impl;

import com.turkcell.planservice.event.PlanCreatedEvent;
import com.turkcell.planservice.event.PlanUpdatedEvent;
import com.turkcell.planservice.service.PlanProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanProducerImpl implements PlanProducer {

    private final StreamBridge streamBridge;

    @Override
    public void sendPlanCreatedEvent(PlanCreatedEvent event) {
        boolean result = streamBridge.send("planCreatedFunction-out-0", event);
        if (result) {
            System.out.println("✅ Plan Created Event Kafka'ya başarıyla gönderildi: " + event);
        } else {
            System.err.println("❌ Plan Created Event Kafka'ya gönderilemedi!");
        }
    }

    @Override
    public void sendPlanUpdatedEvent(PlanUpdatedEvent event) {
        boolean result = streamBridge.send("planUpdatedFunction-out-0", event);
        if (result) {
            System.out.println("✅ Plan Updated Event Kafka'ya başarıyla gönderildi: " + event);
        } else {
            System.err.println("❌ Plan Updated Event Kafka'ya gönderilemedi!");
        }
    }
}
