package com.turkcell.analytics_service.repository;

import com.turkcell.analytics_service.enums.ActivityType;
import com.turkcell.analytics_service.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {
    // Kullanıcı ID'sine göre aktiviteleri bulma
    List<UserActivity> findByUserId(UUID userId);

    // Aktivite türüne göre aktiviteleri bulur (LOGIN, LOGOUT, PAGE_VIEW, vb.)
    List<UserActivity> findByActivityType(ActivityType activityType);

    // IP adresine göre aktiviteleri bulur
    List<UserActivity> findByIpAddress(String ipAddress);

    List<UserActivity> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
}
