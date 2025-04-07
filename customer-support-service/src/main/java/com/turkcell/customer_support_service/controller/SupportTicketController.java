package com.turkcell.customer_support_service.controller;

import com.turkcell.customer_support_service.entity.SupportTicket;
import com.turkcell.customer_support_service.enums.TicketCategory;
import com.turkcell.customer_support_service.enums.TicketStatus;
import com.turkcell.customer_support_service.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @PostMapping
    public ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicket ticket) {
        return new ResponseEntity<>(supportTicketService.createTicket(ticket), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> getTicketById(@PathVariable UUID id) {
        return ResponseEntity.ok(supportTicketService.getTicketById(id));
    }

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(supportTicketService.getAllTickets());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SupportTicket>> getTicketsByCustomerId(@PathVariable UUID customerId) {
        return ResponseEntity.ok(supportTicketService.getTicketsByCustomerId(customerId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportTicket>> getTicketsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(supportTicketService.getTicketsByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SupportTicket>> getTicketsByStatus(@PathVariable TicketStatus status) {
        return ResponseEntity.ok(supportTicketService.getTicketsByStatus(status));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SupportTicket>> getTicketsByCategory(@PathVariable TicketCategory category) {
        return ResponseEntity.ok(supportTicketService.getTicketsByCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportTicket> updateTicket(
            @PathVariable UUID id,
            @RequestBody SupportTicket ticket) {
        return ResponseEntity.ok(supportTicketService.updateTicket(id, ticket));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SupportTicket> updateTicketStatusWithPut(
            @PathVariable UUID id,
            @RequestParam TicketStatus status) {
        return ResponseEntity.ok(supportTicketService.updateTicketStatus(id, status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SupportTicket> updateTicketStatus(
            @PathVariable UUID id,
            @RequestBody TicketStatus status) {
        return ResponseEntity.ok(supportTicketService.updateTicketStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        supportTicketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}