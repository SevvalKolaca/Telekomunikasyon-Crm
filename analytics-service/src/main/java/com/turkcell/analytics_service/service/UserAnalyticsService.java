package com.turkcell.analytics_service.service;

import java.util.List;

import com.turkcell.analytics_service.entity.UserActivity;
import com.turkcell.analytics_service.event.CustomerEvent;

import io.github.ergulberke.enums.AccountStatus;

public interface UserAnalyticsService {
    void processUserCreation(CustomerEvent event);

    void processUserUpdate(CustomerEvent event);

    void processUserDeletion(CustomerEvent event);

    void processUserLogin(CustomerEvent event);

    void processUserLogout(CustomerEvent event);

    void processUserStatusChange(CustomerEvent event, AccountStatus newStatus);

    List<UserActivity> getUserActivitiesByUserId(String userId);
}