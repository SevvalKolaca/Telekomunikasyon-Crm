package com.turkcell.analytics_service.integration;

import io.github.ergulberke.event.customer.CustomerCreatedEvent;
import io.github.ergulberke.event.user.UserLoginEvent;
import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.service.UserActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.MimeTypeUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Kafka entegrasyonunun test sınıfı
 * Bu test, gelen Kafka mesajlarının doğru işlenip işlenmediğini test eder
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.cloud.stream.bindings.userLoginFunction-in-0.destination=user-logins",
        "spring.cloud.stream.bindings.customerCreatedFunction-in-0.destination=customers",
        "spring.cloud.function.definition=userLoginFunction;customerCreatedFunction",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
@Import(TestChannelBinderConfiguration.class)
public class KafkaIntegrationTest {

    @Autowired
    private InputDestination input;

    @MockBean
    private UserActivityService userActivityService;

    /**
     * UserLoginEvent mesajının doğru alınıp işlendiğini test eder
     */
    @Test
    void testUserLoginEventConsumption() {
        UserLoginEvent event = new UserLoginEvent();
        event.setUserId(UUID.randomUUID().toString());
        event.setEmail("test@example.com");
        event.setLoginTime(LocalDateTime.now());
        event.setIpAddress("127.0.0.1");
        event.setDeviceInfo("Test Agent");

        Message<UserLoginEvent> message = MessageBuilder.withPayload(event)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();

        input.send(message, "user-logins");

        verify(userActivityService, times(1)).logActivity(any(UserActivityDto.class));
    }

    /**
     * CustomerCreatedEvent mesajının doğru alınıp işlendiğini test eder
     */
    @Test
    void testCustomerCreatedEventConsumption() {
        CustomerCreatedEvent event = CustomerCreatedEvent.builder()
                .customerId(UUID.randomUUID())
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .timestamp(LocalDateTime.now())
                .build();

        Message<CustomerCreatedEvent> message = MessageBuilder.withPayload(event)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();

        input.send(message, "customers");

        verify(userActivityService, times(1)).logCustomerCreatedActivity(any(CustomerCreatedEvent.class));
    }

    /**
     * Hatalı mesaj durumunda istisna fırlatılmadığını ve uygulamanın çökmediğini
     * test eder
     */
    @Test
    void testInvalidMessageHandling() {
        String invalidJson = "{\"invalid\": true}";
        Message<String> invalidMessage = MessageBuilder.withPayload(invalidJson)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();

        input.send(invalidMessage, "user-logins");

        verify(userActivityService, never()).logActivity(any(UserActivityDto.class));
    }
}