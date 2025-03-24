package io.github.ergulberke.event.customer;

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
public class CustomerCreatedEvent {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String status;
    private String eventType;
    private LocalDateTime timestamp;
}
