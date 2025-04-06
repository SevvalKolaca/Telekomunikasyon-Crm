package com.turkcell.analytics_service.service;

import io.github.ergulberke.event.billingPayment.BillCreatedEvent;
import io.github.ergulberke.event.contract.ContractCreatedEvent;
import io.github.ergulberke.event.plan.PlanCreatedEvent;

public interface SubscriptionAnalyticsService {

    // Yeni abonelik veya sözleşme oluşturulduğunda abonelik analizini kaydet
    void saveSubscriptionAnalytics(ContractCreatedEvent event);

    // Fatura oluşturma veya ödeme yapıldığında ilgili analitiği işle
    void processBillingEvent(BillCreatedEvent event);

    void savePlanAnalytics(PlanCreatedEvent event);

}
