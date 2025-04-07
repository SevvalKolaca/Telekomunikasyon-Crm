package com.turkcell.customer_support_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.customer_support_service.client.CustomerClient;
import com.turkcell.customer_support_service.client.UserClient;
import com.turkcell.customer_support_service.config.TestSecurityConfig;
import com.turkcell.customer_support_service.entity.SupportTicket;
import com.turkcell.customer_support_service.enums.TicketCategory;
import com.turkcell.customer_support_service.enums.TicketPriority;
import com.turkcell.customer_support_service.enums.TicketStatus;
import com.turkcell.customer_support_service.service.SupportTicketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupportTicketController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class SupportTicketControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private SupportTicketService supportTicketService;

        @MockBean
        private CustomerClient customerClient;

        @MockBean
        private UserClient userClient;

        @Test
        void createTicket_ShouldReturnCreatedTicket() throws Exception {
                // Arrange
                UUID customerId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();
                UUID ticketId = UUID.randomUUID();

                SupportTicket requestTicket = SupportTicket.builder()
                                .customerId(customerId)
                                .userId(userId)
                                .subject("Test Ticket")
                                .description("Test Description")
                                .category(TicketCategory.TECHNICAL_SUPPORT)
                                .priority(TicketPriority.HIGH)
                                .build();

                SupportTicket createdTicket = SupportTicket.builder()
                                .id(ticketId)
                                .customerId(customerId)
                                .userId(userId)
                                .subject("Test Ticket")
                                .description("Test Description")
                                .category(TicketCategory.TECHNICAL_SUPPORT)
                                .priority(TicketPriority.HIGH)
                                .status(TicketStatus.OPEN)
                                .build();

                when(customerClient.checkCustomerExists(customerId)).thenReturn(true);
                when(userClient.checkUserExists(userId)).thenReturn(true);
                when(supportTicketService.createTicket(Mockito.any(SupportTicket.class))).thenReturn(createdTicket);

                // Act & Assert
                mockMvc.perform(post("/api/v1/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestTicket)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id", is(ticketId.toString())))
                                .andExpect(jsonPath("$.customerId", is(customerId.toString())))
                                .andExpect(jsonPath("$.userId", is(userId.toString())))
                                .andExpect(jsonPath("$.subject", is("Test Ticket")))
                                .andExpect(jsonPath("$.description", is("Test Description")))
                                .andExpect(jsonPath("$.category", is("TECHNICAL_SUPPORT")))
                                .andExpect(jsonPath("$.priority", is("HIGH")))
                                .andExpect(jsonPath("$.status", is("OPEN")));

                verify(supportTicketService).createTicket(any(SupportTicket.class));
        }

        @Test
        void createTicket_WhenCustomerDoesNotExist_ShouldReturnBadRequest() throws Exception {
                // Arrange
                UUID customerId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();

                SupportTicket requestTicket = SupportTicket.builder()
                                .customerId(customerId)
                                .userId(userId)
                                .subject("Test Ticket")
                                .description("Test Description")
                                .category(TicketCategory.TECHNICAL_SUPPORT)
                                .priority(TicketPriority.HIGH)
                                .build();

                when(supportTicketService.createTicket(any(SupportTicket.class)))
                                .thenThrow(new RuntimeException("Customer not found with id: " + customerId));

                // Act & Assert
                mockMvc.perform(post("/api/v1/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestTicket)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message", containsString("Customer not found")));

                verify(supportTicketService).createTicket(any(SupportTicket.class));
        }

        @Test
        void getTicketById_ShouldReturnTicket() throws Exception {
                // Arrange
                UUID ticketId = UUID.randomUUID();
                UUID customerId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();

                SupportTicket ticket = SupportTicket.builder()
                                .id(ticketId)
                                .customerId(customerId)
                                .userId(userId)
                                .subject("Test Ticket")
                                .description("Test Description")
                                .category(TicketCategory.TECHNICAL_SUPPORT)
                                .priority(TicketPriority.HIGH)
                                .status(TicketStatus.OPEN)
                                .build();

                when(supportTicketService.getTicketById(ticketId)).thenReturn(ticket);

                // Act & Assert
                mockMvc.perform(get("/api/v1/tickets/{id}", ticketId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(ticketId.toString())))
                                .andExpect(jsonPath("$.customerId", is(customerId.toString())))
                                .andExpect(jsonPath("$.userId", is(userId.toString())));

                verify(supportTicketService).getTicketById(ticketId);
        }

        @Test
        void getTicketById_WhenTicketNotFound_ShouldReturnNotFound() throws Exception {
                // Arrange
                UUID ticketId = UUID.randomUUID();

                when(supportTicketService.getTicketById(ticketId))
                                .thenThrow(new RuntimeException("Ticket not found with id: " + ticketId));

                // Act & Assert
                mockMvc.perform(get("/api/v1/tickets/{id}", ticketId))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message", containsString("Ticket not found")));

                verify(supportTicketService).getTicketById(ticketId);
        }

        @Test
        void getTicketsByCustomerId_ShouldReturnTickets() throws Exception {
                // Arrange
                UUID customerId = UUID.randomUUID();

                List<SupportTicket> tickets = Arrays.asList(
                                SupportTicket.builder().id(UUID.randomUUID()).customerId(customerId).build(),
                                SupportTicket.builder().id(UUID.randomUUID()).customerId(customerId).build());

                when(supportTicketService.getTicketsByCustomerId(customerId)).thenReturn(tickets);

                // Act & Assert
                mockMvc.perform(get("/api/v1/tickets/customer/{customerId}", customerId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].customerId", is(customerId.toString())))
                                .andExpect(jsonPath("$[1].customerId", is(customerId.toString())));

                verify(supportTicketService).getTicketsByCustomerId(customerId);
        }

        @Test
        void updateTicketStatus_ShouldReturnUpdatedTicket() throws Exception {
                // Arrange
                UUID ticketId = UUID.randomUUID();
                UUID customerId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();

                SupportTicket updatedTicket = SupportTicket.builder()
                                .id(ticketId)
                                .customerId(customerId)
                                .userId(userId)
                                .subject("Test Ticket")
                                .description("Test Description")
                                .category(TicketCategory.TECHNICAL_SUPPORT)
                                .priority(TicketPriority.HIGH)
                                .status(TicketStatus.IN_PROGRESS)
                                .build();

                when(supportTicketService.updateTicketStatus(eq(ticketId), eq(TicketStatus.IN_PROGRESS)))
                                .thenReturn(updatedTicket);

                // Act & Assert
                mockMvc.perform(patch("/api/v1/tickets/{id}/status", ticketId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("\"IN_PROGRESS\""))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(ticketId.toString())))
                                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));

                verify(supportTicketService).updateTicketStatus(ticketId, TicketStatus.IN_PROGRESS);
        }

        @Test
        void updateTicketStatus_WhenTicketNotFound_ShouldReturnNotFound() throws Exception {
                // Arrange
                UUID ticketId = UUID.randomUUID();

                when(supportTicketService.updateTicketStatus(eq(ticketId), any(TicketStatus.class)))
                                .thenThrow(new RuntimeException("Ticket not found with id: " + ticketId));

                // Act & Assert
                mockMvc.perform(patch("/api/v1/tickets/{id}/status", ticketId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("\"IN_PROGRESS\""))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message", containsString("Ticket not found")));

                verify(supportTicketService).updateTicketStatus(eq(ticketId), any(TicketStatus.class));
        }
}