package com.turkcell.analytics_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEventDTO {

    @NotNull(message = "Plan ID boş olamaz")
    private UUID planId;

    @NotNull(message = "Kullanıcı ID boş olamaz")
    private UUID userId;

    @NotNull(message = "Olay tipi boş olamaz")
    private String eventType;

    @NotNull(message = "Olay tarihi boş olamaz")
    private LocalDateTime eventDate;

    @PositiveOrZero(message = "Tutar negatif olamaz")
    private Double amount;

    private String description;
}