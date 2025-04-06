package com.turkcell.analytics_service.controller;

import com.turkcell.analytics_service.config.SecurityConfig;
import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.service.UserActivityService;
import io.github.ergulberke.event.user.UserLoginEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IntegrationTestController.class)
@Import(SecurityConfig.class)
public class IntegrationTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserActivityService userActivityService;

    @MockBean
    private StreamBridge streamBridge;

    @Test
    void healthCheckShouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/api/test/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("analytics-service"));
    }

    @Test
    void kafkaStatusShouldReturnUpWhenKafkaIsAvailable() throws Exception {
        when(streamBridge.send(anyString(), any())).thenReturn(true);

        mockMvc.perform(get("/api/test/kafka-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.message").value("Kafka bağlantısı aktif"));

        verify(streamBridge, times(1)).send(anyString(), any());
    }

    @Test
    void kafkaStatusShouldReturnDownWhenKafkaIsUnavailable() throws Exception {
        when(streamBridge.send(anyString(), any())).thenReturn(false);

        mockMvc.perform(get("/api/test/kafka-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DOWN"))
                .andExpect(jsonPath("$.message").value("Kafka bağlantısı başarısız"));

        verify(streamBridge, times(1)).send(anyString(), any());
    }

    @Test
    void kafkaStatusShouldReturnErrorWhenExceptionOccurs() throws Exception {
        when(streamBridge.send(anyString(), any())).thenThrow(new RuntimeException("Test exception"));

        mockMvc.perform(get("/api/test/kafka-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").isString());

        verify(streamBridge, times(1)).send(anyString(), any());
    }

    @Test
    void sendTestUserLoginEventShouldReturnSuccessWhenKafkaIsAvailable() throws Exception {
        when(streamBridge.send(anyString(), any(UserLoginEvent.class))).thenReturn(true);

        mockMvc.perform(post("/api/test/send-user-login-event")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Test olay başarıyla Kafka'ya gönderildi"));

        verify(streamBridge, times(1)).send(anyString(), any(UserLoginEvent.class));
    }

    @Test
    void sendTestUserLoginEventShouldReturnErrorWhenKafkaIsUnavailable() throws Exception {
        when(streamBridge.send(anyString(), any(UserLoginEvent.class))).thenReturn(false);

        mockMvc.perform(post("/api/test/send-user-login-event")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Test olay gönderilemedi"));

        verify(streamBridge, times(1)).send(anyString(), any(UserLoginEvent.class));
    }

    @Test
    void directServiceTestShouldReturnSuccess() throws Exception {
        doNothing().when(userActivityService).logActivity(any(UserActivityDto.class));

        mockMvc.perform(post("/api/test/direct-service-test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("UserActivityService testi başarılı"));

        verify(userActivityService, times(1)).logActivity(any(UserActivityDto.class));
    }

    @Test
    void directServiceTestShouldReturnErrorWhenServiceFails() throws Exception {
        doThrow(new RuntimeException("Test exception")).when(userActivityService)
                .logActivity(any(UserActivityDto.class));

        mockMvc.perform(post("/api/test/direct-service-test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").isString());

        verify(userActivityService, times(1)).logActivity(any(UserActivityDto.class));
    }
}