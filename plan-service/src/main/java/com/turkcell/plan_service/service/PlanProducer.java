package com.turkcell.plan_service.service;

import io.github.ergulberke.event.plan.PlanCreatedEvent;

public interface PlanProducer {
    void sendPlanCreatedEvent(PlanCreatedEvent event);
}
