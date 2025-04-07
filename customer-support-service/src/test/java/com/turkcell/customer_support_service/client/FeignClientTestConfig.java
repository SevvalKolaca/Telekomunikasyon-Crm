package com.turkcell.customer_support_service.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.mockito.Mockito.mock;

@TestConfiguration
@ActiveProfiles("test")
public class FeignClientTestConfig {

    @Bean(destroyMethod = "stop")
    public WireMockServer wireMockServer() {
        WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());
        server.start();
        return server;
    }

    @Bean
    public CustomerClient customerClient(WireMockServer server) {
        return new CustomerClientTestImpl(server);
    }

    @Bean
    public UserClient userClient(WireMockServer server) {
        return new UserClientTestImpl(server);
    }

    public static class CustomerClientTestImpl implements CustomerClient {
        private final WireMockServer server;

        public CustomerClientTestImpl(WireMockServer server) {
            this.server = server;
        }

        @Override
        public boolean checkCustomerExists(java.util.UUID id) {
            try {
                String url = String.format("http://localhost:%d/api/v1/customers/%s/exists",
                        server.port(), id);

                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new java.net.URL(url)
                        .openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == 500) {
                    throw new RuntimeException("Server returned 500 status code");
                }

                java.util.Scanner scanner = new java.util.Scanner(connection.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                return Boolean.parseBoolean(response);
            } catch (java.io.IOException e) {
                throw new RuntimeException("Error connecting to server", e);
            }
        }
    }

    public static class UserClientTestImpl implements UserClient {
        private final WireMockServer server;

        public UserClientTestImpl(WireMockServer server) {
            this.server = server;
        }

        @Override
        public boolean checkUserExists(java.util.UUID id) {
            try {
                String url = String.format("http://localhost:%d/api/v1/users/%s/exists",
                        server.port(), id);

                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new java.net.URL(url)
                        .openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == 500) {
                    throw new RuntimeException("Server returned 500 status code");
                }

                java.util.Scanner scanner = new java.util.Scanner(connection.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                return Boolean.parseBoolean(response);
            } catch (java.io.IOException e) {
                throw new RuntimeException("Error connecting to server", e);
            }
        }
    }
}