package com.turkcell.analytics_service.service;

import com.turkcell.analytics_service.dto.UserActivityDto;

import io.github.ergulberke.event.customer.CustomerCreatedEvent;

public interface UserActivityService {
    void logActivity(UserActivityDto activityDto); // Kullanıcı aktivitelerini kaydet

    void logCustomerCreatedActivity(CustomerCreatedEvent event); // Müşteri oluşturulma aktivitesini kaydet
}