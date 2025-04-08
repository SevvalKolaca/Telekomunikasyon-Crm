package com.turkcell.planservice.controller;

import com.turkcell.planservice.dto.request.CreatePlanRequest;
import com.turkcell.planservice.dto.request.UpdatePlanRequest;
import com.turkcell.planservice.dto.response.PlanResponse;
import com.turkcell.planservice.enums.PlanStatus;
import com.turkcell.planservice.enums.PlanType;
import com.turkcell.planservice.service.PlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlanService planService;

    private final UUID planId = UUID.randomUUID();

    @Test
    @WithMockUser
    void getAllPlans_ShouldReturnOk() throws Exception {
        // Given
        PlanResponse planResponse = createPlanResponse();
        when(planService.getAllPlans()).thenReturn(List.of(planResponse));

        // When & Then
        mockMvc.perform(get("/api/plans"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(planResponse.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(planResponse.getName()))
                .andExpect(jsonPath("$[0].description").value(planResponse.getDescription()))
                .andExpect(jsonPath("$[0].price").value(planResponse.getPrice()))
                .andExpect(jsonPath("$[0].type").value(planResponse.getType().toString()))
                .andExpect(jsonPath("$[0].status").value(planResponse.getStatus().toString()));

        verify(planService, times(1)).getAllPlans();
    }

    @Test
    @WithMockUser
    void getPlanById_WhenPlanExists_ShouldReturnOk() throws Exception {
        // Given
        PlanResponse planResponse = createPlanResponse();
        when(planService.getPlanById(planId)).thenReturn(planResponse);

        // When & Then
        mockMvc.perform(get("/api/plans/{id}", planId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(planResponse.getId().toString()))
                .andExpect(jsonPath("$.name").value(planResponse.getName()))
                .andExpect(jsonPath("$.price").value(planResponse.getPrice()))
                .andExpect(jsonPath("$.type").value(planResponse.getType().toString()))
                .andExpect(jsonPath("$.status").value(planResponse.getStatus().toString()));

        verify(planService, times(1)).getPlanById(planId);
    }

    @Test
    @WithMockUser
    void getPlansByStatus_ShouldReturnOk() throws Exception {
        // Given
        PlanResponse planResponse = createPlanResponse();
        when(planService.getPlansByStatus(PlanStatus.ACTIVE)).thenReturn(List.of(planResponse));

        // When & Then
        mockMvc.perform(get("/api/plans/status/{status}", PlanStatus.ACTIVE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(planResponse.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(planResponse.getName()))
                .andExpect(jsonPath("$[0].price").value(planResponse.getPrice()))
                .andExpect(jsonPath("$[0].type").value(planResponse.getType().toString()))
                .andExpect(jsonPath("$[0].status").value(planResponse.getStatus().toString()));

        verify(planService, times(1)).getPlansByStatus(PlanStatus.ACTIVE);
    }

    @Test
    @WithMockUser
    void getPlansByType_ShouldReturnOk() throws Exception {
        // Given
        PlanResponse planResponse = createPlanResponse();
        when(planService.getPlansByType(PlanType.PREPAID)).thenReturn(List.of(planResponse));

        // When & Then
        mockMvc.perform(get("/api/plans/type/{type}", PlanType.PREPAID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(planResponse.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(planResponse.getName()))
                .andExpect(jsonPath("$[0].price").value(planResponse.getPrice()))
                .andExpect(jsonPath("$[0].type").value(planResponse.getType().toString()))
                .andExpect(jsonPath("$[0].status").value(planResponse.getStatus().toString()));

        verify(planService, times(1)).getPlansByType(PlanType.PREPAID);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPlan_WithValidRequest_ShouldReturnCreated() throws Exception {
        // Given
        CreatePlanRequest request = new CreatePlanRequest();
        request.setName("Test Plan");
        request.setDescription("Test Plan Description");
        request.setPrice(BigDecimal.valueOf(100));
        request.setType(PlanType.PREPAID);
        request.setSmsLimit(100);
        request.setInternetLimit(1024);
        request.setVoiceLimit(1000);

        PlanResponse response = createPlanResponse();
        when(planService.createPlan(any(CreatePlanRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.getId().toString()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.description").value(response.getDescription()))
                .andExpect(jsonPath("$.price").value(response.getPrice()))
                .andExpect(jsonPath("$.type").value(response.getType().toString()))
                .andExpect(jsonPath("$.status").value(response.getStatus().toString()));

        verify(planService, times(1)).createPlan(any(CreatePlanRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePlan_WithValidRequest_ShouldReturnOk() throws Exception {
        // Given
        UpdatePlanRequest request = new UpdatePlanRequest();
        request.setId(planId);
        request.setName("Updated Plan");
        request.setPrice(BigDecimal.valueOf(150));
        request.setType(PlanType.POSTPAID);
        request.setStatus(PlanStatus.ACTIVE);
        request.setSmsLimit(200);
        request.setInternetLimit(2048);
        request.setVoiceLimit(2000);

        PlanResponse response = createPlanResponse();
        when(planService.updatePlan(any(UpdatePlanRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(put("/api/plans/{id}", planId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.getId().toString()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.price").value(response.getPrice()))
                .andExpect(jsonPath("$.type").value(response.getType().toString()))
                .andExpect(jsonPath("$.status").value(response.getStatus().toString()));

        verify(planService, times(1)).updatePlan(any(UpdatePlanRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePlan_WhenPlanExists_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(planService).deletePlan(planId);

        // When & Then
        mockMvc.perform(delete("/api/plans/{id}", planId))
                .andExpect(status().isNoContent());

        verify(planService, times(1)).deletePlan(planId);
    }

    @Test
    @WithMockUser(roles = "USER")
    void createPlan_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        CreatePlanRequest request = new CreatePlanRequest();
        request.setName("");
        request.setDescription("");
        request.setPrice(BigDecimal.valueOf(-100));
        request.setType(null);
        request.setSmsLimit(null);
        request.setInternetLimit(null);
        request.setVoiceLimit(null);

        // When & Then
        mockMvc.perform(post("/api/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(planService, never()).createPlan(any(CreatePlanRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createPlan_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        // Given
        CreatePlanRequest request = new CreatePlanRequest();
        request.setName("Test Plan");
        request.setDescription("Test Plan Description");
        request.setPrice(BigDecimal.valueOf(100));
        request.setType(PlanType.PREPAID);
        request.setSmsLimit(100);
        request.setInternetLimit(1024);
        request.setVoiceLimit(1000);

        // When & Then
        mockMvc.perform(post("/api/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verify(planService, never()).createPlan(any(CreatePlanRequest.class));
    }

    @Test
    @WithMockUser
    void getPlanById_WithInvalidUUID_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/plans/invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(planService, never()).getPlanById(any(UUID.class));
    }

    @Test
    @WithMockUser
    void getPlansByStatus_WithInvalidStatus_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/plans/status/INVALID_STATUS"))
                .andExpect(status().isBadRequest());

        verify(planService, never()).getPlansByStatus(any(PlanStatus.class));
    }

    @Test
    @WithMockUser
    void getPlansByType_WithInvalidType_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/plans/type/INVALID_TYPE"))
                .andExpect(status().isBadRequest());

        verify(planService, never()).getPlansByType(any(PlanType.class));
    }

    private PlanResponse createPlanResponse() {
        return PlanResponse.builder()
                .id(planId)
                .name("Test Plan")
                .description("Test Plan Description")
                .price(BigDecimal.valueOf(100))
                .type(PlanType.PREPAID)
                .status(PlanStatus.ACTIVE)
                .smsLimit(100)
                .internetLimit(1024)
                .voiceLimit(1000)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
} 