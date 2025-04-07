package com.turkcell.customer_support_service.service.impl;

import com.turkcell.customer_support_service.client.CustomerClient;
import com.turkcell.customer_support_service.client.UserClient;
import com.turkcell.customer_support_service.entity.SupportTicket;
import com.turkcell.customer_support_service.enums.TicketCategory;
import com.turkcell.customer_support_service.enums.TicketStatus;
import com.turkcell.customer_support_service.repository.SupportTicketRepository;
import com.turkcell.customer_support_service.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final CustomerClient customerClient;
    private final UserClient userClient;
    private final StreamBridge streamBridge;

    @Override
    public SupportTicket createTicket(SupportTicket ticket) {
        // Validate customer and user existence
        if (!customerClient.checkCustomerExists(ticket.getCustomerId())) {
            throw new RuntimeException("Customer not found with id: " + ticket.getCustomerId());
        }
        if (!userClient.checkUserExists(ticket.getUserId())) {
            throw new RuntimeException("User not found with id: " + ticket.getUserId());
        }

        // Set initial status if not set
        if (ticket.getStatus() == null) {
            ticket.setStatus(TicketStatus.OPEN);
        }

        SupportTicket savedTicket = supportTicketRepository.save(ticket);

        // Notify other services through event stream
        streamBridge.send("ticketCreated-out-0", savedTicket);

        return savedTicket;
    }

    @Override
    public SupportTicket getTicketById(UUID id) {
        return supportTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }

    @Override
    public List<SupportTicket> getAllTickets() {
        return supportTicketRepository.findAll();
    }

    @Override
    public List<SupportTicket> getTicketsByCustomerId(UUID customerId) {
        return supportTicketRepository.findByCustomerId(customerId);
    }

    @Override
    public List<SupportTicket> getTicketsByUserId(UUID userId) {
        return supportTicketRepository.findByUserId(userId);
    }

    @Override
    public List<SupportTicket> getTicketsByStatus(TicketStatus status) {
        return supportTicketRepository.findByStatus(status);
    }

    @Override
    public List<SupportTicket> getTicketsByCategory(TicketCategory category) {
        return supportTicketRepository.findByCategory(category);
    }

    @Override
    public SupportTicket updateTicket(UUID id, SupportTicket ticket) {
        SupportTicket existingTicket = getTicketById(id);

        // Update fields
        existingTicket.setSubject(ticket.getSubject());
        existingTicket.setDescription(ticket.getDescription());
        existingTicket.setCategory(ticket.getCategory());
        existingTicket.setPriority(ticket.getPriority());

        return supportTicketRepository.save(existingTicket);
    }

    @Override
    public SupportTicket updateTicketStatus(UUID id, TicketStatus status) {
        SupportTicket ticket = getTicketById(id);
        ticket.setStatus(status);
        SupportTicket updatedTicket = supportTicketRepository.save(ticket);

        // Notify about status change
        streamBridge.send("ticketStatusUpdated-out-0", updatedTicket);

        return updatedTicket;
    }

    @Override
    public void deleteTicket(UUID id) {
        supportTicketRepository.deleteById(id);
    }
}