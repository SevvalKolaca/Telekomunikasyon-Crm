package io.github.ergulberke.event.customer;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreatedEvent {
    private String customerId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String status;
    private String eventType;
    private LocalDateTime timestamp;
}
