package com.turkcell.analytics_service.service.impl;

import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.entity.UserActivity;
import com.turkcell.analytics_service.enums.ActivityType;
import com.turkcell.analytics_service.repository.UserActivityRepository;
import com.turkcell.analytics_service.service.UserActivityService;

import io.github.ergulberke.event.customer.CustomerCreatedEvent;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {

    private final UserActivityRepository userActivityRepository;

    @Override
    public void logActivity(UserActivityDto activityDto) {
        // Kullanıcı aktivitelerini kaydetme işlemi
        System.out.println("Logging user activity: " + activityDto);

        // UserActivityDto'dan UserActivity entity'sine dönüştür
        UserActivity activity = new UserActivity();
        activity.setUserId(activityDto.getUserId());
        activity.setActivityType(activityDto.getActivityType());
        activity.setDescription(activityDto.getDescription());
        activity.setTimestamp(activityDto.getTimestamp());
        activity.setIpAddress(activityDto.getIpAddress());
        activity.setUserAgent(activityDto.getUserAgent());
        activity.setSessionId(activityDto.getSessionId());

        // Veritabanına kaydet
        userActivityRepository.save(activity);
    }

    @Override
    public void logCustomerCreatedActivity(CustomerCreatedEvent event) {
        // CustomerCreatedEvent'ten UserActivity oluştur
        if (event == null) {
            return;
        }

        UserActivity activity = new UserActivity();
        activity.setUserId(event.getCustomerId());
        activity.setActivityType(com.turkcell.analytics_service.enums.ActivityType.CUSTOMER_CREATED);
        activity.setDescription("Yeni müşteri oluşturuldu");
        activity.setTimestamp(java.time.LocalDateTime.now());
        activity.setSessionId(java.util.UUID.randomUUID().toString());

        // Veritabanına kaydet
        userActivityRepository.save(activity);
    }

    @Override
    public List<UserActivity> getAllActivities() {
        return userActivityRepository.findAll();
    }


    @Override
    public List<UserActivity> getActivitiesByDateRange(LocalDateTime start, LocalDateTime end) {
        return userActivityRepository.findByTimestampBetween(start, end);
    }


    @Override
    public List<UserActivity> getActivitiesByUserId(UUID userId) {
        return userActivityRepository.findByUserId(userId);
    }

    @Override
    public List<UserActivity> getActivitiesByActivityType(ActivityType activityType) {
        return userActivityRepository.findByActivityType(activityType);
    }

    @Override
    public List<UserActivity> getActivitiesByIpAddress(String ipAddress) {
        return userActivityRepository.findByIpAddress(ipAddress);
    }
}




