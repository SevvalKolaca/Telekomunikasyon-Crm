package io.github.ergulberke.event.plan;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String eventType;
    private LocalDateTime timestamp;
}
