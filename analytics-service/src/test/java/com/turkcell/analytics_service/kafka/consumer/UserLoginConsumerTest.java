package com.turkcell.analytics_service.kafka.consumer;

import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.service.UserActivityService;
import io.github.ergulberke.event.user.UserLoginEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserLoginConsumerTest {

    @Mock
    private UserActivityService userActivityService;

    @InjectMocks
    private UserLoginConsumer userLoginConsumer;

    private UserLoginEvent userLoginEvent;

    @BeforeEach
    void setUp() {
        // Test için UserLoginEvent oluştur
        userLoginEvent = new UserLoginEvent();
        userLoginEvent.setUserId(UUID.randomUUID().toString());
        userLoginEvent.setEmail("test@example.com");
        userLoginEvent.setLoginTime(LocalDateTime.now());
        userLoginEvent.setIpAddress("127.0.0.1");
        userLoginEvent.setDeviceInfo("Test Browser");
    }

    @Test
    void userLoginFunctionShouldProcessEvent() {
        // Given
        doNothing().when(userActivityService).logActivity(any(UserActivityDto.class));

        // When
        Consumer<UserLoginEvent> consumer = userLoginConsumer.userLoginFunction();
        consumer.accept(userLoginEvent);

        // Then
        ArgumentCaptor<UserActivityDto> dtoCaptor = ArgumentCaptor.forClass(UserActivityDto.class);
        verify(userActivityService, times(1)).logActivity(dtoCaptor.capture());

        UserActivityDto capturedDto = dtoCaptor.getValue();
        assertEquals(UUID.fromString(userLoginEvent.getUserId()), capturedDto.getUserId());
        assertEquals(userLoginEvent.getIpAddress(), capturedDto.getIpAddress());
        assertEquals(userLoginEvent.getDeviceInfo(), capturedDto.getUserAgent());
    }

    @Test
    void userLoginFunctionShouldHandleExceptions() {
        // Given
        doThrow(new RuntimeException("Test exception")).when(userActivityService)
                .logActivity(any(UserActivityDto.class));

        // When/Then
        Consumer<UserLoginEvent> consumer = userLoginConsumer.userLoginFunction();

        // Exception should be caught and not propagated
        assertDoesNotThrow(() -> consumer.accept(userLoginEvent));

        verify(userActivityService, times(1)).logActivity(any(UserActivityDto.class));
    }

    @Test
    void userLoginFunctionShouldHandleNullEvent() {
        // Given
        Consumer<UserLoginEvent> consumer = userLoginConsumer.userLoginFunction();

        // When/Then
        assertDoesNotThrow(() -> consumer.accept(null));

        // Service should not be called with null event
        verify(userActivityService, never()).logActivity(any(UserActivityDto.class));
    }
}