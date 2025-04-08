package com.turkcell.analytics_service.service.impl;

import org.springframework.stereotype.Service;
import com.turkcell.analytics_service.entity.SubscriptionAnalytics;
import com.turkcell.analytics_service.repository.SubscriptionAnalyticsRepository;
import com.turkcell.analytics_service.service.SubscriptionAnalyticsService;

import io.github.ergulberke.enums.AccountStatus;
import io.github.ergulberke.event.billingPayment.BillCreatedEvent;
import io.github.ergulberke.event.contract.ContractCreatedEvent;
import io.github.ergulberke.event.plan.PlanCreatedEvent;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
            analytics.setAccountStatus(AccountStatus.valueOf(event.getStatus()));
            analytics.setStartDate(event.getStartDate().toLocalDate());
            analytics.setEndDate(event.getEndDate().toLocalDate());
            analytics.setRevenue(BigDecimal.valueOf(event.getPrice()));
            analytics.setBillingCycle("MONTHLY"); // Varsayılan değer

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
            analytics.setAccountStatus(AccountStatus.valueOf(event.getStatus())); // Ödeme durumu
            analytics.setStartDate(event.getDueDate()); // Son ödeme tarihi
            analytics.setEndDate(event.getPaymentDate()); // Gerçek ödeme tarihi
            analytics.setRevenue(BigDecimal.valueOf(event.getAmount())); // Fatura tutarı
            analytics.setBillingCycle("MONTHLY"); // Varsayılan değer

            subscriptionAnalyticsRepository.save(analytics);

            log.info("✅ Billing data saved for Customer ID: {}", event.getCustomerId());
        } catch (Exception e) {
            log.error("❌ Failed to process Bill Created Event: {}", event, e);
        }
    }

    @Override
    public void savePlanAnalytics(PlanCreatedEvent event) {
        try {
            SubscriptionAnalytics analytics = new SubscriptionAnalytics();
            analytics.setPlanId(event.getId());
            analytics.setPlanName(event.getName());
            analytics.setRevenue(event.getPrice());
            analytics.setType(event.getType());
            analytics.setPlanStatus(event.getStatus());

            analytics.setSmsLimit(event.getSmsLimit());
            analytics.setInternetLimit(event.getInternetLimit());
            analytics.setVoiceLimit(event.getVoiceLimit());

            analytics.setNewSubscriptions(1); // Yeni plan ilk kez oluşturulmuş, başlangıç olarak 1
            analytics.setCanceledSubscriptions(0); // Henüz iptal edilmemiş
            analytics.setEndDate(null); // Bitiş tarihi belli değil
            analytics.setBillingCycle(event.getType().name().contains("MONTHLY") ? "MONTHLY" : "YEARLY");

            subscriptionAnalyticsRepository.save(analytics);

            log.info("✅ Subscription analytics saved for Plan ID: {}", event.getId());
        } catch (Exception e) {
            log.error("❌ Failed to save plan analytics: {}", event.getId(), e);
        }
    }

    // 🔍 Yeni eklenen analiz sorguları

    @Override
    public List<SubscriptionAnalytics> getAnalyticsByPlanId(UUID planId) {
        return subscriptionAnalyticsRepository.findByPlanId(planId);
    }

    @Override
    public List<SubscriptionAnalytics> getAllAnalytics() {
        return subscriptionAnalyticsRepository.findAll();
    }

    @Override
    public List<SubscriptionAnalytics> getAnalyticsByPlanName(String planName) {
        return subscriptionAnalyticsRepository.findByPlanName(planName);
    }

    @Override
    public List<SubscriptionAnalytics> getAnalyticsByStatus(String status) {
        return subscriptionAnalyticsRepository.findByStatus(status);
    }

    @Override
    public List<SubscriptionAnalytics> getAnalyticsByBillingCycle(String billingCycle) {
        return subscriptionAnalyticsRepository.findByBillingCycle(billingCycle);
    }
}


