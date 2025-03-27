package com.turkcell.notification_service.config;

import io.github.ergulberke.event.user.UserCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class UserEventFunctionConfig {

    @Bean
    public Consumer<UserCreatedEvent> userCreatedEventConsumer() {
        return event -> {
            try {
                // Event işleniyor
                System.out.println("UserEventFunctionConfig: UserCreatedEvent alındı. UserId: "
                        + event.getUserId() + ", Email: " + event.getEmail());
                // Burada event ile ilgili işlemler yapılır (örn: bildirim gönderme)
            } catch (Exception e) {
                System.out.println("UserEventFunctionConfig: UserCreatedEvent işlenemedi. Oluşturulamadı.");
                e.printStackTrace();
            }
        };
    }
}