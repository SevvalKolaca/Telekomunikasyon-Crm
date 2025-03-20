package com.turkcell.analytics_service.service;

import com.turkcell.analytics_service.dto.SubscriptionAnalyticsDTO;
import com.turkcell.analytics_service.dto.SubscriptionEventDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SubscriptionAnalyticsService {

    // Yeni abonelik analiz kaydı oluştur
    SubscriptionAnalyticsDTO createSubscriptionAnalytics(SubscriptionAnalyticsDTO dto);

    // Tüm abonelik analizlerini listele
    List<SubscriptionAnalyticsDTO> getAllSubscriptionAnalytics();

    // Plan ID'sine göre analiz bul
    SubscriptionAnalyticsDTO getSubscriptionAnalyticsByPlanId(UUID planId);

    // Analiz kaydını güncelle
    SubscriptionAnalyticsDTO updateSubscriptionAnalytics(UUID id, SubscriptionAnalyticsDTO dto);

    // Analiz kaydını sil
    void deleteSubscriptionAnalytics(UUID id);
}
