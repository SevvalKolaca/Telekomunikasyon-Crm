package com.turkcell.analytics_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.analytics_service.config.SecurityConfig;
import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.enums.ActivityType;
import com.turkcell.analytics_service.service.UserActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserActivityController.class)
@Import(SecurityConfig.class)
public class UserActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserActivityService userActivityService;

    private UserActivityDto userActivityDto;

    @BeforeEach
    void setUp() {
        userActivityDto = new UserActivityDto();
        userActivityDto.setUserId(UUID.randomUUID());
        userActivityDto.setActivityType(ActivityType.USER_LOGIN);
        userActivityDto.setDescription("Test aktivitesi");
        userActivityDto.setTimestamp(LocalDateTime.now());
        userActivityDto.setIpAddress("127.0.0.1");
        userActivityDto.setUserAgent("Test Agent");
        userActivityDto.setSessionId(UUID.randomUUID().toString());
    }

    @Test
    void logUserActivityShouldReturnOk() throws Exception {
        // Given
        doNothing().when(userActivityService).logActivity(any(UserActivityDto.class));

        // When/Then
        mockMvc.perform(post("/api/user-activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userActivityDto)))
                .andExpect(status().isOk());

        verify(userActivityService, times(1)).logActivity(any(UserActivityDto.class));
    }

    @Test
    void logUserActivityShouldHandleServiceException() throws Exception {
        // Given
        doThrow(new RuntimeException("Test hata")).when(userActivityService).logActivity(any(UserActivityDto.class));

        // When/Then
        mockMvc.perform(post("/api/user-activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userActivityDto)))
                .andExpect(status().is5xxServerError());

        verify(userActivityService, times(1)).logActivity(any(UserActivityDto.class));
    }
}