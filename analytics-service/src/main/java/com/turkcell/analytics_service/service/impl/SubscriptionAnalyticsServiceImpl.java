package com.turkcell.analytics_service.service.impl;

import org.springframework.stereotype.Service;
import com.turkcell.analytics_service.entity.SubscriptionAnalytics;
import com.turkcell.analytics_service.repository.SubscriptionAnalyticsRepository;
import com.turkcell.analytics_service.service.SubscriptionAnalyticsService;
import io.github.ergulberke.event.billingPayment.BillCreatedEvent;
import io.github.ergulberke.event.contract.ContractCreatedEvent;
import io.github.ergulberke.event.plan.PlanCreatedEvent;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubscriptionAnalyticsServiceImpl implements SubscriptionAnalyticsService {

    private final SubscriptionAnalyticsRepository subscriptionAnalyticsRepository;

    @Autowired
    public SubscriptionAnalyticsServiceImpl(SubscriptionAnalyticsRepository subscriptionAnalyticsRepository) {
        this.subscriptionAnalyticsRepository = subscriptionAnalyticsRepository;
    }

    @Override
    public void saveSubscriptionAnalytics(ContractCreatedEvent event) {
        try {
            log.info("📩 Processing Contract Created Event for Subscription Analytics: {}", event.getContractId());

            SubscriptionAnalytics analytics = new SubscriptionAnalytics();
            analytics.setPlanId(event.getPlanId());
            analytics.setPlanName("Plan - " + event.getPlanId());
            analytics.setNewSubscriptions(1); // Yeni abonelik olarak işaretliyoruz
            analytics.setCanceledSubscriptions(0);
            analytics.setStatus(event.getStatus());
            analytics.setStartDate(event.getStartDate().toLocalDate());
            analytics.setEndDate(event.getEndDate().toLocalDate());
            analytics.setRevenue(event.getPrice());

            subscriptionAnalyticsRepository.save(analytics);

            log.info("✅ Subscription analytics saved for Contract ID: {}", event.getContractId());
        } catch (Exception e) {
            log.error("❌ Failed to process Contract Created Event: {}", event, e);
        }
    }

    @Override
    public void processBillingEvent(BillCreatedEvent event) {
        try {
            log.info("📩 Processing Billing Event: Bill ID = {}, Customer ID = {}, Amount = {}",
                    event.getId(), event.getCustomerId(), event.getAmount());

            SubscriptionAnalytics analytics = new SubscriptionAnalytics();
            analytics.setPlanId(null); // Faturanın bağlı olduğu plan yoksa null bırakabiliriz.
            analytics.setPlanName("Billing Data");
            analytics.setNewSubscriptions(0);
            analytics.setCanceledSubscriptions(0);
            analytics.setStatus(event.getStatus()); // Ödeme durumu
            analytics.setStartDate(event.getDueDate()); // Son ödeme tarihi
            analytics.setEndDate(event.getPaymentDate()); // Gerçek ödeme tarihi
            analytics.setRevenue(event.getAmount()); // Fatura tutarı

            subscriptionAnalyticsRepository.save(analytics);

            log.info("✅ Billing data saved for Customer ID: {}", event.getCustomerId());
        } catch (Exception e) {
            log.error("❌ Failed to process Bill Created Event: {}", event, e);
        }
    }

    @Override
    public void savePlanAnalytics(PlanCreatedEvent event) {
        try {
            // Plan analytics verisini kaydediyoruz
            SubscriptionAnalytics analytics = new SubscriptionAnalytics();
            analytics.setPlanId(event.getPlanId());
            analytics.setPlanName("Plan - " + event.getPlanId());
            analytics.setNewSubscriptions(1); // Yeni abonelik olarak işaretliyoruz
            analytics.setCanceledSubscriptions(0); // Başlangıçta iptal yok
            analytics.setStatus(event.getStatus());
            analytics.setStartDate(event.getStartDate().toLocalDate());
            analytics.setEndDate(event.getEndDate().toLocalDate());
            analytics.setRevenue(event.getPrice());

            // Subscription analytics verisini veritabanına kaydediyoruz
            subscriptionAnalyticsRepository.save(analytics);

            System.out.println("✅ Subscription analytics saved for Plan ID: " + event.getPlanId());
        } catch (Exception e) {
            System.err.println("❌ Failed to save plan analytics: " + event.getPlanId());
            e.printStackTrace();
        }
    }
}