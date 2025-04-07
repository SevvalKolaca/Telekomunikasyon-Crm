package com.turkcell.customer_support_service.service;

import com.turkcell.customer_support_service.client.CustomerClient;
import com.turkcell.customer_support_service.client.UserClient;
import com.turkcell.customer_support_service.entity.SupportTicket;
import com.turkcell.customer_support_service.enums.TicketCategory;
import com.turkcell.customer_support_service.enums.TicketPriority;
import com.turkcell.customer_support_service.enums.TicketStatus;
import com.turkcell.customer_support_service.repository.SupportTicketRepository;
import com.turkcell.customer_support_service.service.impl.SupportTicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;

import java.util.UUID;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupportTicketServiceTest {

    @Mock
    private SupportTicketRepository supportTicketRepository;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private UserClient userClient;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private SupportTicketServiceImpl supportTicketService;

    private UUID customerId;
    private UUID userId;
    private SupportTicket testTicket;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        userId = UUID.randomUUID();

        testTicket = SupportTicket.builder()
                .customerId(customerId)
                .userId(userId)
                .subject("Test Ticket")
                .description("Test Description")
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .priority(TicketPriority.HIGH)
                .build();
    }

    @Test
    void createTicket_WhenCustomerAndUserExist_ShouldCreateTicket() {
        // Arrange
        when(customerClient.checkCustomerExists(customerId)).thenReturn(true);
        when(userClient.checkUserExists(userId)).thenReturn(true);
        when(supportTicketRepository.save(any(SupportTicket.class))).thenAnswer(invocation -> {
            SupportTicket savedTicket = invocation.getArgument(0);
            savedTicket.setId(UUID.randomUUID());
            savedTicket.setStatus(TicketStatus.OPEN);
            return savedTicket;
        });
        when(streamBridge.send(anyString(), any())).thenReturn(true);

        // Act
        SupportTicket createdTicket = supportTicketService.createTicket(testTicket);

        // Assert
        assertNotNull(createdTicket);
        assertNotNull(createdTicket.getId());
        assertEquals(TicketStatus.OPEN, createdTicket.getStatus());
        verify(customerClient).checkCustomerExists(customerId);
        verify(userClient).checkUserExists(userId);
        verify(supportTicketRepository).save(any(SupportTicket.class));
        verify(streamBridge).send(eq("ticketCreated-out-0"), any());
    }

    @Test
    void createTicket_WhenCustomerDoesNotExist_ShouldThrowException() {
        // Arrange
        when(customerClient.checkCustomerExists(customerId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.createTicket(testTicket));
        assertEquals("Customer not found with id: " + customerId, exception.getMessage());

        verify(customerClient).checkCustomerExists(customerId);
        verify(userClient, never()).checkUserExists(any());
        verify(supportTicketRepository, never()).save(any());
        verify(streamBridge, never()).send(anyString(), any());
    }

    @Test
    void createTicket_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(customerClient.checkCustomerExists(customerId)).thenReturn(true);
        when(userClient.checkUserExists(userId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.createTicket(testTicket));
        assertEquals("User not found with id: " + userId, exception.getMessage());

        verify(customerClient).checkCustomerExists(customerId);
        verify(userClient).checkUserExists(userId);
        verify(supportTicketRepository, never()).save(any());
        verify(streamBridge, never()).send(anyString(), any());
    }

    @Test
    void createTicket_WhenCustomerClientThrowsException_ShouldPropagateException() {
        // Arrange
        when(customerClient.checkCustomerExists(customerId)).thenThrow(new RuntimeException("Customer service down"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.createTicket(testTicket));
        assertEquals("Customer service down", exception.getMessage());

        verify(customerClient).checkCustomerExists(customerId);
        verify(userClient, never()).checkUserExists(any());
        verify(supportTicketRepository, never()).save(any());
        verify(streamBridge, never()).send(anyString(), any());
    }

    @Test
    void createTicket_WhenUserClientThrowsException_ShouldPropagateException() {
        // Arrange
        when(customerClient.checkCustomerExists(customerId)).thenReturn(true);
        when(userClient.checkUserExists(userId)).thenThrow(new RuntimeException("User service down"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.createTicket(testTicket));
        assertEquals("User service down", exception.getMessage());

        verify(customerClient).checkCustomerExists(customerId);
        verify(userClient).checkUserExists(userId);
        verify(supportTicketRepository, never()).save(any());
        verify(streamBridge, never()).send(anyString(), any());
    }

    @Test
    void getTicketsByCustomerId_ShouldReturnTickets() {
        // Arrange
        List<SupportTicket> expectedTickets = Arrays.asList(
                SupportTicket.builder().id(UUID.randomUUID()).customerId(customerId).build(),
                SupportTicket.builder().id(UUID.randomUUID()).customerId(customerId).build());

        when(supportTicketRepository.findByCustomerId(customerId)).thenReturn(expectedTickets);

        // Act
        List<SupportTicket> actualTickets = supportTicketService.getTicketsByCustomerId(customerId);

        // Assert
        assertEquals(expectedTickets.size(), actualTickets.size());
        assertEquals(expectedTickets, actualTickets);
        verify(supportTicketRepository).findByCustomerId(customerId);
    }

    @Test
    void getTicketById_WhenTicketExists_ShouldReturnTicket() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        SupportTicket expectedTicket = SupportTicket.builder()
                .id(ticketId)
                .customerId(customerId)
                .userId(userId)
                .build();

        when(supportTicketRepository.findById(ticketId)).thenReturn(Optional.of(expectedTicket));

        // Act
        SupportTicket actualTicket = supportTicketService.getTicketById(ticketId);

        // Assert
        assertEquals(expectedTicket, actualTicket);
        verify(supportTicketRepository).findById(ticketId);
    }

    @Test
    void getTicketById_WhenTicketDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        when(supportTicketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.getTicketById(ticketId));
        assertEquals("Ticket not found with id: " + ticketId, exception.getMessage());

        verify(supportTicketRepository).findById(ticketId);
    }

    @Test
    void updateTicketStatus_WhenTicketExists_ShouldUpdateStatus() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        SupportTicket existingTicket = SupportTicket.builder()
                .id(ticketId)
                .customerId(customerId)
                .userId(userId)
                .status(TicketStatus.OPEN)
                .build();

        when(supportTicketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(supportTicketRepository.save(any(SupportTicket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(streamBridge.send(anyString(), any())).thenReturn(true);

        // Act
        SupportTicket updatedTicket = supportTicketService.updateTicketStatus(ticketId, TicketStatus.IN_PROGRESS);

        // Assert
        assertEquals(TicketStatus.IN_PROGRESS, updatedTicket.getStatus());
        verify(supportTicketRepository).findById(ticketId);
        verify(supportTicketRepository).save(existingTicket);
        verify(streamBridge).send(eq("ticketStatusUpdated-out-0"), any());
    }

    @Test
    void updateTicketStatus_WhenTicketDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        when(supportTicketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.updateTicketStatus(ticketId, TicketStatus.IN_PROGRESS));
        assertEquals("Ticket not found with id: " + ticketId, exception.getMessage());

        verify(supportTicketRepository).findById(ticketId);
        verify(supportTicketRepository, never()).save(any());
        verify(streamBridge, never()).send(anyString(), any());
    }
}