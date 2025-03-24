package io.github.ergulberke.event.plan;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanCreatedEvent {

    private UUID planId;
    private String planName;
    private Double price;
    private String currency;
    private Integer durationInMonths;
    private String features;
    private String eventType;
    private LocalDateTime timestamp;
}
