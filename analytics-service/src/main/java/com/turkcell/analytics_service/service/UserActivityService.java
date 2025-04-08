package com.turkcell.analytics_service.service;

import com.turkcell.analytics_service.dto.UserActivityDto;

import com.turkcell.analytics_service.entity.UserActivity;
import com.turkcell.analytics_service.enums.ActivityType;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserActivityService {
    void logActivity(UserActivityDto activityDto); // Kullanıcı aktivitelerini kaydet

    void logCustomerCreatedActivity(CustomerCreatedEvent event); // Müşteri oluşturulma aktivitesini kaydet

    List<UserActivity> getAllActivities();
    List<UserActivity> getActivitiesByDateRange(LocalDateTime start, LocalDateTime end);
    List<UserActivity> getActivitiesByActivityType(ActivityType activityType);
    List<UserActivity> getActivitiesByIpAddress(String ipAddress);
    List<UserActivity> getActivitiesByUserId(UUID userId);

}