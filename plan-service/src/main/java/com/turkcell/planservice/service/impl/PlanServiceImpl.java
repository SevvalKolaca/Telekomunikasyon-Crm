package com.turkcell.planservice.service.impl;

import com.turkcell.planservice.client.ContractClient;
import com.turkcell.planservice.dto.request.CreatePlanRequest;
import com.turkcell.planservice.dto.request.UpdatePlanRequest;
import com.turkcell.planservice.dto.response.PlanResponse;
import com.turkcell.planservice.entity.Plan;
import com.turkcell.planservice.enums.PlanStatus;
import com.turkcell.planservice.enums.PlanType;
import com.turkcell.planservice.event.PlanCreatedEvent;
import com.turkcell.planservice.event.PlanUpdatedEvent;
import com.turkcell.planservice.exception.PlanNotFoundException;
import com.turkcell.planservice.repository.PlanRepository;
import com.turkcell.planservice.service.PlanProducer;
import com.turkcell.planservice.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final ContractClient contractClient;
    private final PlanProducer planProducer;

    @Override
    public List<PlanResponse> getAllPlans() {
        return planRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PlanResponse getPlanById(UUID id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException(id));
        return mapToResponse(plan);
    }

    @Override
    public List<PlanResponse> getPlansByStatus(PlanStatus status) {
        return planRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanResponse> getPlansByType(PlanType type) {
        return planRepository.findByType(type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanResponse> getPlansByTypeAndStatus(PlanType type, PlanStatus status) {
        return planRepository.findByTypeAndStatus(type, status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlanResponse createPlan(CreatePlanRequest request) {
        Plan plan = new Plan();
        plan.setName(request.getName());
        plan.setPrice(request.getPrice());
        plan.setType(request.getType());
        plan.setStatus(PlanStatus.ACTIVE); // Yeni oluşturulan planlar varsayılan olarak aktif
        plan.setSmsLimit(request.getSmsLimit());
        plan.setInternetLimit(request.getInternetLimit());
        plan.setVoiceLimit(request.getVoiceLimit());

        // Plan kaydediliyor
        Plan savedPlan = planRepository.save(plan);

        // PlanCreatedEvent oluşturuluyor
        PlanCreatedEvent event = new PlanCreatedEvent(
                savedPlan.getId(),
                savedPlan.getName(),
                savedPlan.getPrice(),
                savedPlan.getType(),
                savedPlan.getStatus(),
                savedPlan.getSmsLimit(),
                savedPlan.getInternetLimit(),
                savedPlan.getVoiceLimit(),
                savedPlan.getCreatedAt());

        // Kafka Producer ile event gönderimi
        planProducer.sendPlanCreatedEvent(event); // Kafka'ya gönderiliyor

        return mapToResponse(savedPlan);
    }

    @Override
    @Transactional
    public PlanResponse updatePlan(UpdatePlanRequest request) {
        Plan plan = planRepository.findById(request.getId())
                .orElseThrow(() -> new PlanNotFoundException(request.getId()));

        if (request.getName() != null) {
            plan.setName(request.getName());
        }

        if (request.getPrice() != null) {
            plan.setPrice(request.getPrice());
        }

        if (request.getType() != null) {
            plan.setType(request.getType());
        }

        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        if (request.getSmsLimit() != null) {
            plan.setSmsLimit(request.getSmsLimit());
        }

        if (request.getInternetLimit() != null) {
            plan.setInternetLimit(request.getInternetLimit());
        }

        if (request.getVoiceLimit() != null) {
            plan.setVoiceLimit(request.getVoiceLimit());
        }

        Plan updatedPlan = planRepository.save(plan);

        // Kafka event gönderimi
        PlanUpdatedEvent event = new PlanUpdatedEvent(
                updatedPlan.getId(),
                updatedPlan.getName(),
                updatedPlan.getPrice(),
                updatedPlan.getType(),
                updatedPlan.getStatus(),
                updatedPlan.getSmsLimit(),
                updatedPlan.getInternetLimit(),
                updatedPlan.getVoiceLimit(),
                LocalDateTime.now());

        planProducer.sendPlanUpdatedEvent(event);

        return mapToResponse(updatedPlan);
    }

    @Override
    @Transactional
    public void deletePlan(UUID id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException(id));

        // Planın aktif sözleşmeleri kontrol edilebilir
        // contractClient.getContractsByPlanId(id)

        // Silmek yerine durumunu DEPRECATED olarak işaretleme
        plan.setStatus(PlanStatus.DEPRECATED);
        planRepository.save(plan);
    }

    private PlanResponse mapToResponse(Plan plan) {
        return PlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .price(plan.getPrice())
                .type(plan.getType())
                .status(plan.getStatus())
                .smsLimit(plan.getSmsLimit())
                .internetLimit(plan.getInternetLimit())
                .voiceLimit(plan.getVoiceLimit())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}

