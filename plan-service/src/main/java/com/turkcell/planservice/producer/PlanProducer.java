package com.turkcell.planservice.producer;

import com.turkcell.planservice.event.PlanCreatedEvent;
import com.turkcell.planservice.event.PlanUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanProducer {

    private final StreamBridge streamBridge;

    public void sendPlanCreatedEvent(PlanCreatedEvent event) {
        streamBridge.send("planCreated-out-0", event);
    }

    public void sendPlanUpdatedEvent(PlanUpdatedEvent event) {
        streamBridge.send("planUpdated-out-0", event);
    }

    public void sendPlanDeletedEvent(PlanUpdatedEvent event) {
        streamBridge.send("planDeleted-out-0", event);
    }
} 