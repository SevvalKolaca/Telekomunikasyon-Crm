package com.turkcell.customer_support_service.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { FeignClientTestConfig.class })
@ActiveProfiles("test")
public class FeignClientTest {

        @Autowired
        private CustomerClient customerClient;

        @Autowired
        private UserClient userClient;

        @Autowired
        private WireMockServer wireMockServer;

        @BeforeEach
        void setup() {
                // Reset WireMock mappings before each test
                wireMockServer.resetAll();
        }

        @AfterEach
        void tearDown() {
                // Verify no pending requests after each test
                wireMockServer.resetAll();
        }

        @Test
        void testCustomerExists_ReturnsTrue() {
                UUID customerId = UUID.randomUUID();

                // Configure stub for customer existence check
                wireMockServer.stubFor(get(urlPathMatching("/api/v1/customers/" + customerId + "/exists"))
                                .willReturn(aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBody("true")));

                // Call client method
                boolean result = customerClient.checkCustomerExists(customerId);

                // Verify result
                assertTrue(result);

                // Verify correct endpoint was called
                wireMockServer.verify(getRequestedFor(urlPathMatching("/api/v1/customers/" + customerId + "/exists")));
        }

        @Test
        void testCustomerExists_ReturnsFalse() {
                UUID customerId = UUID.randomUUID();

                // Configure stub for non-existent customer
                wireMockServer.stubFor(get(urlPathMatching("/api/v1/customers/" + customerId + "/exists"))
                                .willReturn(aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBody("false")));

                // Call client method
                boolean result = customerClient.checkCustomerExists(customerId);

                // Verify result
                assertFalse(result);

                // Verify correct endpoint was called
                wireMockServer.verify(getRequestedFor(urlPathMatching("/api/v1/customers/" + customerId + "/exists")));
        }

        @Test
        void testUserExists_ReturnsTrue() {
                UUID userId = UUID.randomUUID();

                // Configure stub for user existence check
                wireMockServer.stubFor(get(urlPathMatching("/api/v1/users/" + userId + "/exists"))
                                .willReturn(aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBody("true")));

                // Call client method
                boolean result = userClient.checkUserExists(userId);

                // Verify result
                assertTrue(result);

                // Verify correct endpoint was called
                wireMockServer.verify(getRequestedFor(urlPathMatching("/api/v1/users/" + userId + "/exists")));
        }

        @Test
        void testUserExists_ReturnsFalse() {
                UUID userId = UUID.randomUUID();

                // Configure stub for non-existent user
                wireMockServer.stubFor(get(urlPathMatching("/api/v1/users/" + userId + "/exists"))
                                .willReturn(aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBody("false")));

                // Call client method
                boolean result = userClient.checkUserExists(userId);

                // Verify result
                assertFalse(result);

                // Verify correct endpoint was called
                wireMockServer.verify(getRequestedFor(urlPathMatching("/api/v1/users/" + userId + "/exists")));
        }

        @Test
        void testCustomerClient_ServerError() {
                UUID customerId = UUID.randomUUID();

                // Configure stub for server error
                wireMockServer.stubFor(get(urlPathMatching("/api/v1/customers/" + customerId + "/exists"))
                                .willReturn(aResponse()
                                                .withStatus(500)
                                                .withHeader("Content-Type", "application/json")
                                                .withBody("{\"message\":\"Internal server error\"}")));

                // Call client method and verify exception is thrown
                Exception exception = assertThrows(Exception.class, () -> {
                        customerClient.checkCustomerExists(customerId);
                });

                // Verify the exception contains the right status code
                assertTrue(exception.getMessage().contains("500") ||
                                (exception.getCause() != null && exception.getCause().getMessage().contains("500")));
        }

        @Test
        void testUserClient_ServerError() {
                UUID userId = UUID.randomUUID();

                // Configure stub for server error
                wireMockServer.stubFor(get(urlPathMatching("/api/v1/users/" + userId + "/exists"))
                                .willReturn(aResponse()
                                                .withStatus(500)
                                                .withHeader("Content-Type", "application/json")
                                                .withBody("{\"message\":\"Internal server error\"}")));

                // Call client method and verify exception is thrown
                Exception exception = assertThrows(Exception.class, () -> {
                        userClient.checkUserExists(userId);
                });

                // Verify the exception contains the right status code
                assertTrue(exception.getMessage().contains("500") ||
                                (exception.getCause() != null && exception.getCause().getMessage().contains("500")));
        }
}