package com.turkcell.analytics_service.service;

import com.turkcell.analytics_service.entity.SubscriptionAnalytics;
import io.github.ergulberke.event.billingPayment.BillCreatedEvent;
import io.github.ergulberke.event.contract.ContractCreatedEvent;
import io.github.ergulberke.event.plan.PlanCreatedEvent;

import java.util.List;
import java.util.UUID;

public interface SubscriptionAnalyticsService {

    // Yeni abonelik veya sözleşme oluşturulduğunda abonelik analizini kaydet
    void saveSubscriptionAnalytics(ContractCreatedEvent event);

    // Fatura oluşturma veya ödeme yapıldığında ilgili analitiği işle
    void processBillingEvent(BillCreatedEvent event);

    void savePlanAnalytics(PlanCreatedEvent event);

    public List<SubscriptionAnalytics> getAnalyticsByPlanId(UUID planId);
    public List<SubscriptionAnalytics> getAllAnalytics();
    List<SubscriptionAnalytics> getAnalyticsByStatus(String status);
    List<SubscriptionAnalytics> getAnalyticsByPlanName(String planName);
    List<SubscriptionAnalytics> getAnalyticsByBillingCycle(String billingCycle);

}
