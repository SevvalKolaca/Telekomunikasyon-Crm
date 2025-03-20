package com.turkcell.analytics_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.turkcell.analytics_service.entity.UserActivity;
import com.turkcell.analytics_service.enums.ActivityType;
import com.turkcell.analytics_service.event.CustomerEvent;
import com.turkcell.analytics_service.repository.UserActivityRepository;
import com.turkcell.analytics_service.service.UserAnalyticsService;

import io.github.ergulberke.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAnalyticsServiceImpl implements UserAnalyticsService {

    private final UserActivityRepository userActivityRepository;

    @Override
    public void processUserCreation(CustomerEvent event) {
        log.info("Processing user creation event: {}", event);
        saveUserActivity(event, ActivityType.USER_CREATED, "Kullanıcı oluşturuldu");
    }

    @Override
    public void processUserUpdate(CustomerEvent event) {
        log.info("Processing user update event: {}", event);
        saveUserActivity(event, ActivityType.USER_UPDATED, "Kullanıcı güncellendi");
    }

    @Override
    public void processUserDeletion(CustomerEvent event) {
        log.info("Processing user deletion event: {}", event);
        saveUserActivity(event, ActivityType.USER_DELETED, "Kullanıcı silindi");
    }

    @Override
    public void processUserLogin(CustomerEvent event) {
        log.info("Processing user login event: {}", event);
        saveUserActivity(event, ActivityType.USER_LOGIN, "Kullanıcı giriş yaptı");
    }

    @Override
    public void processUserLogout(CustomerEvent event) {
        log.info("Processing user logout event: {}", event);
        saveUserActivity(event, ActivityType.USER_LOGOUT, "Kullanıcı çıkış yaptı");
    }

    @Override
    public void processUserStatusChange(CustomerEvent event, AccountStatus newStatus) {
        log.info("Processing user status change event: {}, new status: {}", event, newStatus);
        String description = "Kullanıcı durumu değişti: " + newStatus.name();

        switch (newStatus) {
            case ACTIVE:
                saveUserActivity(event, ActivityType.USER_ACTIVATED, description);
                break;
            case SUSPENDED:
                saveUserActivity(event, ActivityType.USER_SUSPENDED, description);
                break;
            case CLOSED:
                saveUserActivity(event, ActivityType.USER_CLOSED, description);
                break;
            case PENDING:
                saveUserActivity(event, ActivityType.USER_PENDING, description);
                break;
            case BLACKLISTED:
                saveUserActivity(event, ActivityType.USER_BLACKLISTED, description);
                break;
            default:
                saveUserActivity(event, ActivityType.USER_UPDATED, description);
        }
    }

    @Override
    public List<UserActivity> getUserActivitiesByUserId(String userId) {
        return userActivityRepository.findByUserIdOrderByTimestampDesc(UUID.fromString(userId));
    }

    private void saveUserActivity(CustomerEvent event, ActivityType activityType, String description) {
        try {
            UserActivity activity = UserActivity.builder()
                    .userId(UUID.fromString(event.getUserId()))
                    .activityType(activityType)
                    .timestamp(LocalDateTime.now())
                    .description(description)
                    .build();

            userActivityRepository.save(activity);
            log.info("User activity saved: {}", activity);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format for userId: {}", event.getUserId(), e);
        } catch (Exception e) {
            log.error("Error saving user activity for event: {}", event, e);
        }
    }
}