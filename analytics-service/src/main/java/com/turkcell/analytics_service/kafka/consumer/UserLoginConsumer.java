package com.turkcell.analytics_service.kafka.consumer;

import org.springframework.stereotype.Component;
import com.turkcell.analytics_service.service.UserActivityService;
import io.github.ergulberke.event.user.UserLoginEvent;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import lombok.extern.slf4j.Slf4j;
import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.enums.ActivityType;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class UserLoginConsumer {

    private final UserActivityService userActivityService;

    public UserLoginConsumer(UserActivityService userActivityService) {
        this.userActivityService = userActivityService;
    }

    @Bean
    public Consumer<UserLoginEvent> userLoginFunction() {
        return event -> {
            try {
                // Null ve geçersiz veri kontrolü
                if (event == null || !isValidUserLoginEvent(event)) {
                    log.warn("Null veya geçersiz kullanıcı giriş olayı alındı, işleme atlanıyor");
                    return;
                }

                log.info("📩 Kullanıcı Giriş Olayı alındı: {}", event.getUserId());

                // UserActivityDto oluşturuluyor
                UserActivityDto activityDto = new UserActivityDto();
                String userId = event.getUserId();
                if (userId != null && !userId.isEmpty()) {
                    activityDto.setUserId(UUID.fromString(userId)); // Kullanıcı ID'si
                }
                activityDto.setActivityType(ActivityType.USER_LOGIN); // Aktivite tipi
                activityDto.setDescription("Kullanıcı giriş yaptı.");
                activityDto.setTimestamp(event.getLoginTime() != null ? event.getLoginTime() : LocalDateTime.now()); // Giriş
                                                                                                                     // zamanı
                activityDto.setIpAddress(event.getIpAddress()); // Kullanıcı IP adresi
                activityDto.setUserAgent(event.getDeviceInfo()); // Kullanıcı cihaz bilgisi
                activityDto.setSessionId(UUID.randomUUID().toString()); // Session ID

                // UserActivityService'e kaydediyoruz
                userActivityService.logActivity(activityDto);

                log.info("✅ Kullanıcı giriş olayı başarıyla işlendi: {}", event.getUserId());
            } catch (Exception e) {
                log.error("❌ Kullanıcı giriş olayı işlenirken hata oluştu: {}",
                        event != null ? event.getUserId() : "null", e);
            }
        };
    }

    /**
     * UserLoginEvent nesnesinin geçerli olup olmadığını kontrol eder
     */
    private boolean isValidUserLoginEvent(UserLoginEvent event) {
        return event.getUserId() != null && !event.getUserId().isEmpty() &&
                event.getEmail() != null && !event.getEmail().isEmpty();
    }
}
