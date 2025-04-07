package com.turkcell.customer_support_service.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private Long customerId;
    private String subject;
    private String description;
    private String priority;
    private String status;
    private String category;
    private String assignedTo;
    private String createdAt;
    private String updatedAt;
}