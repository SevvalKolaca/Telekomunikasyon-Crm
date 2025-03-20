package io.github.ergulberke.event.plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanCreatedEvent {
    private String planId;
    private String planName;
    private Double price;
    private String currency;
    private Integer durationInMonths;
    private String features;
    private String eventType; // CREATED, UPDATED, DELETED, PRICE_CHANGED
    private LocalDateTime timestamp;
}