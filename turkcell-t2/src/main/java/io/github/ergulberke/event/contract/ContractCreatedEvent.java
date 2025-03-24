package io.github.ergulberke.event.contract;

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
public class ContractCreatedEvent {
    private UUID contractId;
    private UUID customerId;
    private UUID planId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double price;
    private String currency;
    private String status;
    private String eventType;
    private LocalDateTime timestamp;
}
