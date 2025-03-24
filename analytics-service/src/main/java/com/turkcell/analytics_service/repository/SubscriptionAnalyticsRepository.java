package com.turkcell.analytics_service.repository;

import com.turkcell.analytics_service.entity.SubscriptionAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface SubscriptionAnalyticsRepository extends JpaRepository<SubscriptionAnalytics, UUID> {

    // Plan ID'ye göre tüm abonelik analitiğini bulma
    List<SubscriptionAnalytics> findByPlanId(UUID planId);

    // Plan adına göre tüm abonelik analitiğini bulma
    List<SubscriptionAnalytics> findByPlanName(String planName);

    // Abonelik durumuna göre analizleri getirir (aktif/pasif)
    List<SubscriptionAnalytics> findByStatus(String status);

    // Abonelik döngüsüne göre analizleri getirir (aylık/yıllık)
    List<SubscriptionAnalytics> findByBillingCycle(String billingCycle);

}
