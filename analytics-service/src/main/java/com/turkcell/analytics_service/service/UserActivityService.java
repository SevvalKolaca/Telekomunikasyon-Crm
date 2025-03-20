package com.turkcell.analytics_service.service;

import com.turkcell.analytics_service.dto.UserActivityDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserActivityService {

    // Kullanıcı aktivitesi oluştur
    UserActivityDTO createUserActivity(UserActivityDTO dto);

    // Kullanıcı ID'sine göre aktiviteleri getir
    List<UserActivityDTO> getUserActivitiesByUserId(UUID userId);

    // Tüm aktiviteleri listele
    List<UserActivityDTO> getAllUserActivities();
}
