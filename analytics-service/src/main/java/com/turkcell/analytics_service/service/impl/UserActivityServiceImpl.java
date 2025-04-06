package com.turkcell.analytics_service.service.impl;

import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.entity.UserActivity;
import com.turkcell.analytics_service.repository.UserActivityRepository;
import com.turkcell.analytics_service.service.UserActivityService;

import io.github.ergulberke.event.customer.CustomerCreatedEvent;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

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

}
