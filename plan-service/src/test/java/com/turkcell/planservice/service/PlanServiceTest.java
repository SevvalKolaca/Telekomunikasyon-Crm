package com.turkcell.planservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.turkcell.planservice.client.ContractClient;
import com.turkcell.planservice.client.ContractResponse;
import com.turkcell.planservice.dto.request.CreatePlanRequest;
import com.turkcell.planservice.dto.request.UpdatePlanRequest;
import com.turkcell.planservice.dto.response.PlanResponse;
import com.turkcell.planservice.entity.Plan;
import com.turkcell.planservice.enums.PlanStatus;
import com.turkcell.planservice.enums.PlanType;
import com.turkcell.planservice.exception.PlanBusinessException;
import com.turkcell.planservice.exception.PlanNotFoundException;
import com.turkcell.planservice.repository.PlanRepository;
import com.turkcell.planservice.service.impl.PlanServiceImpl;
import com.turkcell.planservice.service.PlanProducer;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private ContractClient contractClient;

    @Mock
    private PlanProducer planProducer;

    @InjectMocks
    private PlanServiceImpl planService;

    private Plan plan;
    private UUID planId;

    @BeforeEach
    void setUp() {
        planId = UUID.randomUUID();
        plan = Plan.builder()
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

    @Test
    void getAllPlans_ShouldReturnAllPlans() {
        // Given
        when(planRepository.findAll()).thenReturn(List.of(plan));

        // When
        List<PlanResponse> result = planService.getAllPlans();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(planId, result.get(0).getId());
        verify(planRepository).findAll();
    }

    @Test
    void getPlanById_WhenPlanExists_ShouldReturnPlan() {
        // Given
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));

        // When
        PlanResponse result = planService.getPlanById(planId);

        // Then
        assertNotNull(result);
        assertEquals(planId, result.getId());
        verify(planRepository).findById(planId);
    }

    @Test
    void getPlanById_WhenPlanNotExists_ShouldThrowException() {
        // Given
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PlanNotFoundException.class, () -> planService.getPlanById(planId));
        verify(planRepository).findById(planId);
    }

    @Test
    void createPlan_ShouldCreateAndReturnPlan() {
        // Given
        CreatePlanRequest request = new CreatePlanRequest();
        request.setName("New Plan");
        request.setDescription("New Plan Description");
        request.setPrice(BigDecimal.valueOf(200));
        request.setType(PlanType.POSTPAID);
        request.setSmsLimit(200);
        request.setInternetLimit(2048);
        request.setVoiceLimit(2000);

        when(planRepository.existsByName(request.getName())).thenReturn(false);
        when(planRepository.save(any(Plan.class))).thenReturn(plan);
        doNothing().when(planProducer).sendPlanCreatedEvent(any());

        // When
        PlanResponse result = planService.createPlan(request);

        // Then
        assertNotNull(result);
        assertEquals(planId, result.getId());
        verify(planRepository).save(any(Plan.class));
        verify(planProducer).sendPlanCreatedEvent(any());
    }

    @Test
    void updatePlan_WhenPlanExists_ShouldUpdateAndReturnPlan() {
        // Given
        UpdatePlanRequest request = new UpdatePlanRequest();
        request.setId(planId);
        request.setName("Updated Plan");
        request.setDescription("Updated Plan Description");
        request.setPrice(BigDecimal.valueOf(300));
        request.setType(PlanType.HYBRID);
        request.setStatus(PlanStatus.INACTIVE);
        request.setSmsLimit(300);
        request.setInternetLimit(3072);
        request.setVoiceLimit(3000);

        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(planRepository.save(any(Plan.class))).thenReturn(plan);
        doNothing().when(planProducer).sendPlanUpdatedEvent(any());

        // When
        PlanResponse result = planService.updatePlan(request);

        // Then
        assertNotNull(result);
        assertEquals(planId, result.getId());
        verify(planRepository).findById(planId);
        verify(planRepository).save(any(Plan.class));
        verify(planProducer).sendPlanUpdatedEvent(any());
    }

    @Test
    void updatePlan_WhenPlanNotExists_ShouldThrowException() {
        // Given
        UpdatePlanRequest request = new UpdatePlanRequest();
        request.setId(planId);

        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PlanNotFoundException.class, () -> planService.updatePlan(request));
        verify(planRepository).findById(planId);
        verify(planRepository, never()).save(any(Plan.class));
    }

    @Test
    void deletePlan_WhenPlanExists_ShouldUpdateStatusToDeprecated() {
        // Given
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(contractClient.getContractsByPlanId(planId)).thenReturn(ResponseEntity.ok(List.of()));
        when(planRepository.save(any(Plan.class))).thenReturn(plan);
        doNothing().when(planProducer).sendPlanUpdatedEvent(any());

        // When
        planService.deletePlan(planId);

        // Then
        verify(planRepository).findById(planId);
        verify(planRepository).save(any(Plan.class));
        verify(planProducer).sendPlanUpdatedEvent(any());
        assertEquals(PlanStatus.DEPRECATED, plan.getStatus());
    }

    @Test
    void deletePlan_WhenPlanNotExists_ShouldThrowException() {
        // Given
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PlanNotFoundException.class, () -> planService.deletePlan(planId));
        verify(planRepository).findById(planId);
        verify(planRepository, never()).save(any(Plan.class));
    }

    @Test
    void createPlan_WithInvalidPrice_ShouldThrowException() {
        // Given
        CreatePlanRequest request = new CreatePlanRequest();
        request.setName("Invalid Price Plan");
        request.setDescription("Invalid Price Plan Description");
        request.setPrice(BigDecimal.valueOf(-100)); // Negatif fiyat
        request.setType(PlanType.PREPAID);

        // When & Then
        assertThrows(PlanBusinessException.class, () -> planService.createPlan(request));
        verify(planRepository, never()).save(any(Plan.class));
        verify(planProducer, never()).sendPlanCreatedEvent(any());
    }

    @Test
    void createPlan_WithInvalidLimits_ShouldThrowException() {
        // Given
        CreatePlanRequest request = new CreatePlanRequest();
        request.setName("Invalid Limits Plan");
        request.setDescription("Invalid Limits Plan Description");
        request.setPrice(BigDecimal.valueOf(100));
        request.setType(PlanType.PREPAID);
        request.setSmsLimit(-100); // Negatif limit
        request.setInternetLimit(-1024);
        request.setVoiceLimit(-1000);

        // When & Then
        assertThrows(PlanBusinessException.class, () -> planService.createPlan(request));
        verify(planRepository, never()).save(any(Plan.class));
        verify(planProducer, never()).sendPlanCreatedEvent(any());
    }

    @Test
    void createPlan_WithExistingName_ShouldThrowException() {
        // Given
        CreatePlanRequest request = new CreatePlanRequest();
        request.setName("Test Plan"); // Mevcut plan ile aynı isim
        request.setDescription("Test Plan Description");
        request.setPrice(BigDecimal.valueOf(100));
        request.setType(PlanType.PREPAID);

        when(planRepository.existsByName(request.getName())).thenReturn(true);

        // When & Then
        assertThrows(PlanBusinessException.class, () -> planService.createPlan(request));
        verify(planRepository, never()).save(any(Plan.class));
        verify(planProducer, never()).sendPlanCreatedEvent(any());
    }

    @Test
    void deletePlan_WithActiveContracts_ShouldThrowException() {
        // Given
        ContractResponse contractResponse = new ContractResponse();
        contractResponse.setId(UUID.randomUUID());
        
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(contractClient.getContractsByPlanId(planId))
                .thenReturn(ResponseEntity.ok(List.of(contractResponse))); // Aktif sözleşme var

        // When & Then
        assertThrows(PlanBusinessException.class, () -> planService.deletePlan(planId));
        verify(planRepository).findById(planId);
        verify(planRepository, never()).save(any(Plan.class));
        verify(planProducer, never()).sendPlanUpdatedEvent(any());
    }
} 