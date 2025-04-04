package com.turkcell.customer_support_service.service;

import com.turkcell.customer_support_service.entity.SupportTicket;
import com.turkcell.customer_support_service.enums.TicketCategory;
import com.turkcell.customer_support_service.enums.TicketStatus;

import java.util.List;
import java.util.UUID;

public interface SupportTicketService {
    SupportTicket createTicket(SupportTicket ticket);

    SupportTicket getTicketById(UUID id);

    List<SupportTicket> getAllTickets();

    List<SupportTicket> getTicketsByCustomerId(UUID customerId);

    List<SupportTicket> getTicketsByUserId(UUID userId);

    List<SupportTicket> getTicketsByStatus(TicketStatus status);

    List<SupportTicket> getTicketsByCategory(TicketCategory category);

    SupportTicket updateTicket(UUID id, SupportTicket ticket);

    SupportTicket updateTicketStatus(UUID id, TicketStatus status);

    void deleteTicket(UUID id);
}