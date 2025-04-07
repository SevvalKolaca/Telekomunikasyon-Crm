package com.turkcell.customer_support_service.integration;

import com.turkcell.customer_support_service.client.CustomerClient;
import com.turkcell.customer_support_service.client.UserClient;
import com.turkcell.customer_support_service.entity.SupportTicket;
import com.turkcell.customer_support_service.enums.TicketCategory;
import com.turkcell.customer_support_service.enums.TicketPriority;
import com.turkcell.customer_support_service.enums.TicketStatus;
import com.turkcell.customer_support_service.service.SupportTicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.config.import-check.enabled=false"
})
@TestPropertySource(locations = "classpath:application-test.yml")
@Import(TestConfiguration.class)
@ActiveProfiles("test")
public class SupportTicketIntegrationTest {

    @Autowired
    private SupportTicketService supportTicketService;

    @MockBean
    private CustomerClient customerClient;

    @MockBean
    private UserClient userClient;

    @Test
    void testCreateTicket_WhenCustomerAndUserExist() {
        // Test verisi hazırla
        UUID customerId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        SupportTicket ticket = SupportTicket.builder()
                .customerId(customerId)
                .userId(userId)
                .subject("Test Ticket")
                .description("Test Description")
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .priority(TicketPriority.HIGH)
                .build();

        // Mock davranışları tanımla
        when(customerClient.checkCustomerExists(customerId)).thenReturn(true);
        when(userClient.checkUserExists(userId)).thenReturn(true);

        // Test et
        SupportTicket createdTicket = supportTicketService.createTicket(ticket);

        // Doğrula
        assertNotNull(createdTicket);
        assertEquals(TicketStatus.OPEN, createdTicket.getStatus());
        verify(customerClient).checkCustomerExists(customerId);
        verify(userClient).checkUserExists(userId);
    }

    @Test
    void testCreateTicket_WhenCustomerDoesNotExist() {
        // Test verisi hazırla
        UUID customerId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        SupportTicket ticket = SupportTicket.builder()
                .customerId(customerId)
                .userId(userId)
                .subject("Test Ticket")
                .description("Test Description")
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .priority(TicketPriority.HIGH)
                .build();

        // Mock davranışları tanımla
        when(customerClient.checkCustomerExists(customerId)).thenReturn(false);
        when(userClient.checkUserExists(userId)).thenReturn(true);

        // Test et ve exception'ı doğrula
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.createTicket(ticket));
        assertEquals("Customer not found with id: " + customerId, exception.getMessage());

        // Feign client çağrılarını doğrula
        verify(customerClient).checkCustomerExists(customerId);
        verify(userClient, never()).checkUserExists(any());
    }

    @Test
    void testCreateTicket_WhenUserDoesNotExist() {
        // Test verisi hazırla
        UUID customerId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        SupportTicket ticket = SupportTicket.builder()
                .customerId(customerId)
                .userId(userId)
                .subject("Test Ticket")
                .description("Test Description")
                .category(TicketCategory.TECHNICAL_SUPPORT)
                .priority(TicketPriority.HIGH)
                .build();

        // Mock davranışları tanımla
        when(customerClient.checkCustomerExists(customerId)).thenReturn(true);
        when(userClient.checkUserExists(userId)).thenReturn(false);

        // Test et ve exception'ı doğrula
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supportTicketService.createTicket(ticket));
        assertEquals("User not found with id: " + userId, exception.getMessage());

        // Feign client çağrılarını doğrula
        verify(customerClient).checkCustomerExists(customerId);
        verify(userClient).checkUserExists(userId);
    }

    @Test
    void testCreateTicket_WithInvalidData() {
        // Test verisi hazırla - eksik alanlarla
        SupportTicket ticket = new SupportTicket();
        // Sadece customerId ve userId set edildi, diğer zorunlu alanlar boş
        ticket.setCustomerId(UUID.randomUUID());
        ticket.setUserId(UUID.randomUUID());

        // Mock davranışları tanımla
        when(customerClient.checkCustomerExists(any())).thenReturn(true);
        when(userClient.checkUserExists(any())).thenReturn(true);

        // Test et ve exception'ı doğrula
        assertThrows(Exception.class, () -> supportTicketService.createTicket(ticket));
    }
}