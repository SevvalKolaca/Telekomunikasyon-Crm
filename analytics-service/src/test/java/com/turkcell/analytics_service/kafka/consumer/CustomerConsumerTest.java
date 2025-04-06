package com.turkcell.analytics_service.kafka.consumer;

import com.turkcell.analytics_service.service.UserActivityService;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerConsumerTest {

    @Mock
    private UserActivityService userActivityService;

    @InjectMocks
    private CustomerConsumer customerConsumer;

    private CustomerCreatedEvent customerEvent;

    @BeforeEach
    void setUp() {
        // Test için CustomerCreatedEvent oluştur
        customerEvent = new CustomerCreatedEvent();
        // CustomerCreatedEvent özelliklerini doldur
    }

    @Test
    void customerFunctionShouldProcessEvent() {
        // Given
        doNothing().when(userActivityService).logCustomerCreatedActivity(any());

        // When
        Consumer<CustomerCreatedEvent> consumer = customerConsumer.customerCreatedFunction();
        consumer.accept(customerEvent);

        // Then
        verify(userActivityService, times(1)).logCustomerCreatedActivity(any());
    }

    @Test
    void customerFunctionShouldHandleExceptions() {
        // Given
        doThrow(new RuntimeException("Test exception"))
                .when(userActivityService).logCustomerCreatedActivity(any());

        // When/Then
        Consumer<CustomerCreatedEvent> consumer = customerConsumer.customerCreatedFunction();

        // Exception should be caught and not propagated
        assertDoesNotThrow(() -> consumer.accept(customerEvent));

        verify(userActivityService, times(1)).logCustomerCreatedActivity(any());
    }

    @Test
    void customerFunctionShouldHandleNullEvent() {
        // Given
        Consumer<CustomerCreatedEvent> consumer = customerConsumer.customerCreatedFunction();

        // When/Then
        assertDoesNotThrow(() -> consumer.accept(null));

        // Service should not be called with null event
        verify(userActivityService, never()).logCustomerCreatedActivity(any());
    }
}