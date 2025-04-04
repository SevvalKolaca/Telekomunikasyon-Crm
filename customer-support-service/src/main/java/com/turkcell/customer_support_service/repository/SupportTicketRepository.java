package com.turkcell.customer_support_service.repository;

import com.turkcell.customer_support_service.entity.SupportTicket;
import com.turkcell.customer_support_service.enums.TicketCategory;
import com.turkcell.customer_support_service.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID> {
    List<SupportTicket> findByCustomerId(UUID customerId);

    List<SupportTicket> findByUserId(UUID userId);

    List<SupportTicket> findByStatus(TicketStatus status);

    List<SupportTicket> findByCategory(TicketCategory category);

    List<SupportTicket> findByCustomerIdAndStatus(UUID customerId, TicketStatus status);

    List<SupportTicket> findByUserIdAndStatus(UUID userId, TicketStatus status);
}