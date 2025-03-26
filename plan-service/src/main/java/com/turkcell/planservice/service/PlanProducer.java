package com.turkcell.planservice.service;

import com.turkcell.planservice.event.PlanCreatedEvent;
import com.turkcell.planservice.event.PlanUpdatedEvent;

public interface PlanProducer {
    void sendPlanCreatedEvent(PlanCreatedEvent event);

    void sendPlanUpdatedEvent(PlanUpdatedEvent event);
}
