package com.turkcell.analytics_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.turkcell.analytics_service.enums.ActivityType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityDTO {

    private UUID id;

    @NotNull(message = "Kullanıcı ID boş olamaz")
    private UUID userId;

    @NotNull(message = "Aktivite tipi boş olamaz")
    private ActivityType activityType;

    @NotNull(message = "Zaman bilgisi boş olamaz")
    private LocalDateTime timestamp;

    private String description;

    @NotBlank(message = "IP adresi boş olamaz")
    private String ipAddress;

    @NotBlank(message = "User agent bilgisi boş olamaz")
    private String userAgent;

    @NotBlank(message = "Session ID boş olamaz")
    private String sessionId;
}