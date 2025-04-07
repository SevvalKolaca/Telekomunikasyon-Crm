package io.github.ergulberke.event.plan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import io.github.ergulberke.enums.PlanStatus;
import io.github.ergulberke.enums.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanCreatedEvent {
    private UUID id;
    private String name;
    private BigDecimal price;
    private PlanType type;
    private PlanStatus status;
    private Integer smsLimit;
    private Integer internetLimit;
    private Integer voiceLimit;
    private LocalDateTime createdAt;

}
