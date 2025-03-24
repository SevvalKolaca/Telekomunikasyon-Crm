package com.turkcell.analytics_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.github.ergulberke.enums.AccountStatus;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionAnalyticsDTO {

    @NotNull(message = "Plan ID boş olamaz")
    private UUID planId;

    @NotNull(message = "Plan adı boş olamaz")
    private String planName;

    @PositiveOrZero(message = "Yeni abonelik sayısı negatif olamaz")
    private Integer newSubscriptions;

    @PositiveOrZero(message = "İptal edilen abonelik sayısı negatif olamaz")
    private Integer canceledSubscriptions;

    @NotNull(message = "Durum boş olamaz")
    @Pattern(regexp = "^(ACTIVE|PASSIVE)$", message = "Geçersiz durum")
    private AccountStatus status;

    @NotNull(message = "Başlangıç tarihi boş olamaz")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Faturalandırma döngüsü boş olamaz")
    @Pattern(regexp = "^(MONTHLY|YEARLY)$", message = "Geçersiz faturalandırma döngüsü")
    private String billingCycle;
}