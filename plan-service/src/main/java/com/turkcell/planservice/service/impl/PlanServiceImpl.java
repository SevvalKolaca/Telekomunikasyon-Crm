package com.turkcell.planservice.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turkcell.planservice.client.ContractClient;
import com.turkcell.planservice.client.ContractResponse;
import com.turkcell.planservice.dto.request.CreatePlanRequest;
import com.turkcell.planservice.dto.request.UpdatePlanRequest;
import com.turkcell.planservice.dto.response.PlanResponse;
import com.turkcell.planservice.entity.Plan;
import com.turkcell.planservice.enums.PlanStatus;
import com.turkcell.planservice.enums.PlanType;
import com.turkcell.planservice.event.PlanCreatedEvent;
import com.turkcell.planservice.event.PlanUpdatedEvent;
import com.turkcell.planservice.exception.PlanBusinessException;
import com.turkcell.planservice.exception.PlanNotFoundException;
import com.turkcell.planservice.repository.PlanRepository;
import com.turkcell.planservice.service.PlanProducer;
import com.turkcell.planservice.service.PlanService;

import lombok.RequiredArgsConstructor;

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
        // Validasyonlar
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlanBusinessException("Plan price must be greater than zero");
        }

        if (request.getSmsLimit() != null && request.getSmsLimit() < 0) {
            throw new PlanBusinessException("SMS limit cannot be negative");
        }

        if (request.getInternetLimit() != null && request.getInternetLimit() < 0) {
            throw new PlanBusinessException("Internet limit cannot be negative");
        }

        if (request.getVoiceLimit() != null && request.getVoiceLimit() < 0) {
            throw new PlanBusinessException("Voice limit cannot be negative");
        }

        if (planRepository.existsByName(request.getName())) {
            throw new PlanBusinessException("Plan with name " + request.getName() + " already exists");
        }

        Plan plan = new Plan();
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setPrice(request.getPrice());
        plan.setType(request.getType());
        plan.setStatus(PlanStatus.ACTIVE); // Yeni oluşturulan planlar varsayılan olarak aktif
        plan.setSmsLimit(request.getSmsLimit());
        plan.setInternetLimit(request.getInternetLimit());
        plan.setVoiceLimit(request.getVoiceLimit());

        // Plan kaydediliyor
        Plan savedPlan = planRepository.save(plan);

        // Kafka event gönderimi
        PlanCreatedEvent event = PlanCreatedEvent.builder()
                .id(savedPlan.getId())
                .name(savedPlan.getName())
                .description(savedPlan.getDescription())
                .price(savedPlan.getPrice())
                .type(savedPlan.getType())
                .status(savedPlan.getStatus())
                .smsLimit(savedPlan.getSmsLimit())
                .internetLimit(savedPlan.getInternetLimit())
                .voiceLimit(savedPlan.getVoiceLimit())
                .createdAt(savedPlan.getCreatedAt())
                .build();
        planProducer.sendPlanCreatedEvent(event);

        return mapToResponse(savedPlan);
    }

    @Override
    @Transactional
    public PlanResponse updatePlan(UpdatePlanRequest request) {
        Plan plan = planRepository.findById(request.getId())
                .orElseThrow(() -> new PlanNotFoundException(request.getId()));

        // Validasyonlar
        if (request.getPrice() != null && request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlanBusinessException("Plan price must be greater than zero");
        }

        if (request.getSmsLimit() != null && request.getSmsLimit() < 0) {
            throw new PlanBusinessException("SMS limit cannot be negative");
        }

        if (request.getInternetLimit() != null && request.getInternetLimit() < 0) {
            throw new PlanBusinessException("Internet limit cannot be negative");
        }

        if (request.getVoiceLimit() != null && request.getVoiceLimit() < 0) {
            throw new PlanBusinessException("Voice limit cannot be negative");
        }

        if (request.getName() != null && !request.getName().equals(plan.getName()) && planRepository.existsByName(request.getName())) {
            throw new PlanBusinessException("Plan with name " + request.getName() + " already exists");
        }

        if (request.getName() != null) {
            plan.setName(request.getName());
        }

        if (request.getDescription() != null) {
            plan.setDescription(request.getDescription());
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
        PlanUpdatedEvent event = PlanUpdatedEvent.builder()
                .id(updatedPlan.getId())
                .name(updatedPlan.getName())
                .price(updatedPlan.getPrice())
                .type(updatedPlan.getType())
                .status(updatedPlan.getStatus())
                .smsLimit(updatedPlan.getSmsLimit())
                .internetLimit(updatedPlan.getInternetLimit())
                .voiceLimit(updatedPlan.getVoiceLimit())
                .updatedAt(LocalDateTime.now())
                .build();
        planProducer.sendPlanUpdatedEvent(event);

        return mapToResponse(updatedPlan);
    }

    @Override
    @Transactional
    public void deletePlan(UUID id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException(id));

        // Planın aktif sözleşmeleri kontrol ediliyor
        ResponseEntity<List<ContractResponse>> contractsResponse = contractClient.getContractsByPlanId(id);
        if (contractsResponse.getBody() != null && !contractsResponse.getBody().isEmpty()) {
            throw new PlanBusinessException("Plan has active contracts and cannot be deleted");
        }

        // Silmek yerine durumunu DEPRECATED olarak işaretleme
        plan.setStatus(PlanStatus.DEPRECATED);
        Plan updatedPlan = planRepository.save(plan);

        // Kafka event gönderimi
        PlanUpdatedEvent event = PlanUpdatedEvent.builder()
                .id(updatedPlan.getId())
                .name(updatedPlan.getName())
                .price(updatedPlan.getPrice())
                .type(updatedPlan.getType())
                .status(updatedPlan.getStatus())
                .smsLimit(updatedPlan.getSmsLimit())
                .internetLimit(updatedPlan.getInternetLimit())
                .voiceLimit(updatedPlan.getVoiceLimit())
                .updatedAt(LocalDateTime.now())
                .build();
        planProducer.sendPlanUpdatedEvent(event);
    }

    @Override
    public boolean isPlanActive(UUID id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException(id));
        return plan.getStatus() == PlanStatus.ACTIVE;
    }

    private PlanResponse mapToResponse(Plan plan) {
        PlanResponse response = new PlanResponse();
        response.setId(plan.getId());
        response.setName(plan.getName());
        response.setDescription(plan.getDescription());
        response.setPrice(plan.getPrice());
        response.setType(plan.getType());
        response.setStatus(plan.getStatus());
        response.setSmsLimit(plan.getSmsLimit());
        response.setInternetLimit(plan.getInternetLimit());
        response.setVoiceLimit(plan.getVoiceLimit());
        response.setCreatedAt(plan.getCreatedAt());
        response.setUpdatedAt(plan.getUpdatedAt());
        return response;
    }
}

