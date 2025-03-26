package com.turkcell.analytics_service.kafka.consumer;

import org.springframework.stereotype.Component;
import com.turkcell.analytics_service.service.UserActivityService;
import io.github.ergulberke.event.customer.UserLoginEvent;
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
                log.info("📩 User Login Event received: {}", event.getCustomerId());

                // UserActivityDto oluşturuluyor
                UserActivityDto activityDto = new UserActivityDto();
                activityDto.setUserId(UUID.fromString(event.getCustomerId())); // Kullanıcı ID'si
                activityDto.setActivityType(ActivityType.USER_LOGIN); // Aktivite tipi
                activityDto.setDescription("User logged in.");
                activityDto.setTimestamp(LocalDateTime.now()); // Aktivite zaman bilgisi
                activityDto.setIpAddress(event.getIpAddress()); // Kullanıcı IP adresi
                activityDto.setUserAgent(event.getUserAgent()); // Kullanıcı user-agent bilgisi
                activityDto.setSessionId(UUID.randomUUID().toString()); // Session ID

                // UserActivityService'e kaydediyoruz
                userActivityService.logActivity(activityDto);

                log.info("✅ User login event successfully processed: {}", event.getCustomerId());
            } catch (Exception e) {
                log.error("❌ User login event processing failed: {}", event.getCustomerId(), e);
            }
        };
    }
}
