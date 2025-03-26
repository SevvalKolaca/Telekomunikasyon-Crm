package com.turkcell.analytics_service.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionAnalyticsDto {
    private UUID planId;
    private String planName;
    private Integer newSubscriptions;
    private Integer canceledSubscriptions;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double revenue;
}

