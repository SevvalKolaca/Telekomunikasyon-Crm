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
                // Null kontrolü ekle
                if (event == null) {
                    log.warn("Null müşteri olayı alındı, işleme atlanıyor");
                    return;
                }

                log.info("📩 Customer Created Event received: {}", event.getCustomerId());

                // UserActivityService'e kaydediyoruz
                userActivityService.logCustomerCreatedActivity(event);

                log.info("✅ Customer event successfully processed: {}", event.getCustomerId());
            } catch (Exception e) {
                log.error("❌ Customer event processing failed: {}", event != null ? event.getCustomerId() : "null", e);
            }
        };
    }
}
