package com.turkcell.customer_support_service.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleClientTest {

    private WireMockServer wireMockServer;
    private CustomerClient customerClient;
    private UserClient userClient;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        customerClient = new FeignClientTestConfig.CustomerClientTestImpl(wireMockServer);
        userClient = new FeignClientTestConfig.UserClientTestImpl(wireMockServer);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testCustomerExists_ReturnsTrue() {
        UUID customerId = UUID.randomUUID();

        wireMockServer.stubFor(get(urlPathMatching("/api/v1/customers/" + customerId + "/exists"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));

        boolean result = customerClient.checkCustomerExists(customerId);

        assertTrue(result);
        wireMockServer.verify(getRequestedFor(urlPathMatching("/api/v1/customers/" + customerId + "/exists")));
    }

    @Test
    void testCustomerExists_ReturnsFalse() {
        UUID customerId = UUID.randomUUID();

        wireMockServer.stubFor(get(urlPathMatching("/api/v1/customers/" + customerId + "/exists"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")));

        boolean result = customerClient.checkCustomerExists(customerId);

        assertFalse(result);
        wireMockServer.verify(getRequestedFor(urlPathMatching("/api/v1/customers/" + customerId + "/exists")));
    }

    @Test
    void testUserExists_ReturnsTrue() {
        UUID userId = UUID.randomUUID();

        wireMockServer.stubFor(get(urlPathMatching("/api/v1/users/" + userId + "/exists"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));

        boolean result = userClient.checkUserExists(userId);

        assertTrue(result);
        wireMockServer.verify(getRequestedFor(urlPathMatching("/api/v1/users/" + userId + "/exists")));
    }

    @Test
    void testUserExists_ReturnsFalse() {
        UUID userId = UUID.randomUUID();

        wireMockServer.stubFor(get(urlPathMatching("/api/v1/users/" + userId + "/exists"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")));

        boolean result = userClient.checkUserExists(userId);

        assertFalse(result);
        wireMockServer.verify(getRequestedFor(urlPathMatching("/api/v1/users/" + userId + "/exists")));
    }

    @Test
    void testCustomerClient_ServerError() {
        UUID customerId = UUID.randomUUID();

        wireMockServer.stubFor(get(urlPathMatching("/api/v1/customers/" + customerId + "/exists"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\":\"Internal server error\"}")));

        Exception exception = assertThrows(Exception.class, () -> {
            customerClient.checkCustomerExists(customerId);
        });

        assertTrue(exception.getMessage().contains("500") ||
                (exception.getCause() != null && exception.getCause().getMessage().contains("500")));
    }

    @Test
    void testUserClient_ServerError() {
        UUID userId = UUID.randomUUID();

        wireMockServer.stubFor(get(urlPathMatching("/api/v1/users/" + userId + "/exists"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\":\"Internal server error\"}")));

        Exception exception = assertThrows(Exception.class, () -> {
            userClient.checkUserExists(userId);
        });

        assertTrue(exception.getMessage().contains("500") ||
                (exception.getCause() != null && exception.getCause().getMessage().contains("500")));
    }
}