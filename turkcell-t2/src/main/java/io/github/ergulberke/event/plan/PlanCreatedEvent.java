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
    private String name;
    private Double price;
    private String currency;
    private int duration;
    private String features;
    private String eventType;
    private LocalDateTime timestamp;
}
