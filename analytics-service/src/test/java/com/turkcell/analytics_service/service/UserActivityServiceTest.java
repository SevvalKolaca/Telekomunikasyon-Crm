package com.turkcell.analytics_service.service;

import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.entity.UserActivity;
import com.turkcell.analytics_service.enums.ActivityType;
import com.turkcell.analytics_service.repository.UserActivityRepository;
import com.turkcell.analytics_service.service.impl.UserActivityServiceImpl;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserActivityServiceTest {

    @Mock
    private UserActivityRepository userActivityRepository;

    @InjectMocks
    private UserActivityServiceImpl userActivityService;

    private UserActivityDto activityDto;
    private CustomerCreatedEvent customerEvent;

    @BeforeEach
    void setUp() {
        // Test için UserActivityDto oluştur
        activityDto = new UserActivityDto();
        activityDto.setUserId(UUID.randomUUID());
        activityDto.setActivityType(ActivityType.USER_LOGIN);
        activityDto.setDescription("Test aktivitesi");
        activityDto.setTimestamp(LocalDateTime.now());
        activityDto.setIpAddress("127.0.0.1");
        activityDto.setUserAgent("Test Agent");
        activityDto.setSessionId(UUID.randomUUID().toString());

        // Test için CustomerCreatedEvent oluştur
        customerEvent = new CustomerCreatedEvent();
        // CustomerCreatedEvent özelliklerini doldur
    }

    @Test
    void logActivityShouldSaveActivity() {
        // Given
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn(new UserActivity());

        // When
        userActivityService.logActivity(activityDto);

        // Then
        ArgumentCaptor<UserActivity> activityCaptor = ArgumentCaptor.forClass(UserActivity.class);
        verify(userActivityRepository, times(1)).save(activityCaptor.capture());

        UserActivity capturedActivity = activityCaptor.getValue();
        assertEquals(activityDto.getUserId(), capturedActivity.getUserId());
        assertEquals(activityDto.getActivityType(), capturedActivity.getActivityType());
        assertEquals(activityDto.getDescription(), capturedActivity.getDescription());
    }

    @Test
    void logCustomerCreatedActivityShouldSaveActivity() {
        // Given
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn(new UserActivity());

        // When
        userActivityService.logCustomerCreatedActivity(customerEvent);

        // Then
        verify(userActivityRepository, times(1)).save(any(UserActivity.class));
    }

    @Test
    void logActivityShouldHandleNullValues() {
        // Given
        UserActivityDto nullDto = new UserActivityDto();
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn(new UserActivity());

        // When & Then
        assertDoesNotThrow(() -> userActivityService.logActivity(nullDto));
        verify(userActivityRepository, times(1)).save(any(UserActivity.class));
    }
}