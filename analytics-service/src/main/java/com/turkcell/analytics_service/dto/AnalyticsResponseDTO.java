package com.turkcell.analytics_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponseDTO {

    private LocalDateTime reportDate;

    private Map<String, Double> metrics;

    private Map<String, Map<String, Long>> distributions;

    private String status;

    private String message;
}