package com.turkcell.analytics_service.service;

import io.github.ergulberke.event.contract.ContractCreatedEvent;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;
import io.github.ergulberke.event.plan.PlanCreatedEvent;

public interface AnalyticsService {
    void savePlanAnalytics(PlanCreatedEvent event);

    void saveCustomerAnalytics(CustomerCreatedEvent event); // Customer verisini kaydetme

    void saveContractAnalytics(ContractCreatedEvent event); // Yeni metod

}
