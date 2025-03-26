package com.turkcell.analytics_service.kafka.consumer;

import org.springframework.stereotype.Component;
import com.turkcell.analytics_service.service.UserActivityService;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import lombok.extern.slf4j.Slf4j;
import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.enums.ActivityType;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class CustomerConsumer {

    private final UserActivityService userActivityService;

    public CustomerConsumer(UserActivityService userActivityService) {
        this.userActivityService = userActivityService;
    }

    @Bean
    public Consumer<CustomerCreatedEvent> customerCreatedFunction() {
        return event -> {
            try {
                log.info("📩 Customer Created Event received: {}", event.getCustomerId());

                // UserActivityDto oluşturuluyor
                UserActivityDto activityDto = new UserActivityDto();
                activityDto.setUserId((event.getCustomerId())); // Müşteri ID'si
                activityDto.setActivityType(ActivityType.CUSTOMER_CREATED); // Aktivite tipi
                activityDto.setDescription("A new customer was created.");
                activityDto.setTimestamp(LocalDateTime.now()); // Aktivite zaman bilgisi
                activityDto.setIpAddress("127.0.0.1"); // Örnek IP adresi
                activityDto.setUserAgent("Mozilla/5.0"); // Örnek user-agent
                activityDto.setSessionId(UUID.randomUUID().toString()); // Session ID

                // UserActivityService'e kaydediyoruz
                userActivityService.logActivity(activityDto);

                log.info("✅ Customer event successfully processed: {}", event.getCustomerId());
            } catch (Exception e) {
                log.error("❌ Customer event processing failed: {}", event.getCustomerId(), e);
            }
        };
    }
}
