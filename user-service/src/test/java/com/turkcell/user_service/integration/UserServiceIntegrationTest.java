package com.turkcell.user_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.user_service.dto.create.CreatedUserRequest;
import com.turkcell.user_service.dto.create.CreatedUserResponse;
import com.turkcell.user_service.dto.delete.DeleteUserResponse;
import com.turkcell.user_service.dto.get.GetUserResponse;
import com.turkcell.user_service.dto.getAll.getAllUserResponse;
import com.turkcell.user_service.dto.update.UpdateUserRequest;
import com.turkcell.user_service.dto.update.UpdateUserResponse;
import com.turkcell.user_service.repository.UserRepository;
import io.github.ergulberke.event.user.UserCreatedEvent;
import io.github.ergulberke.jwt.JwtTokenProvider;
import io.github.ergulberke.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import({UserServiceIntegrationTest.DummyJwtTokenProviderConfig.class})
public class UserServiceIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    // Kafka test binder; spring-cloud-stream-test-binder dependency sayesinde kullanılabiliyor.
    @Autowired(required = false)
    private OutputDestination output;

    private static final String AUTH_HEADER = "Authorization";
    private static final String ADMIN_TOKEN = "Bearer dummy-token";
    private static final String NON_ADMIN_TOKEN = "Bearer nonadmin-token";
    private static final String INVALID_TOKEN = "Bearer invalid-token";

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    // 1. Kullanıcı oluşturma: başarılı senaryo
    @Test
    public void testCreateUser_success() {
        CreatedUserRequest request = new CreatedUserRequest();
        request.setFirstname("Alice");
        request.setLastname("Wonderland");
        request.setEmail("alice@example.com");
        request.setPassword("secret");
        request.setPhone("555-1234");

        ResponseEntity<CreatedUserResponse> response =
                restTemplate.postForEntity("/users/create", request, CreatedUserResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        CreatedUserResponse createdUser = response.getBody();
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo("alice@example.com");
    }

    // 2. Duplicate kullanıcı oluşturma: hata senaryosu
    @Test
    public void testCreateUser_duplicate() {
        CreatedUserRequest request = new CreatedUserRequest();
        request.setFirstname("Bob");
        request.setLastname("Builder");
        request.setEmail("bob@example.com");
        request.setPassword("secret");
        request.setPhone("555-5678");

        ResponseEntity<CreatedUserResponse> response1 =
                restTemplate.postForEntity("/users/create", request, CreatedUserResponse.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> response2 =
                restTemplate.postForEntity("/users/create", request, String.class);
        assertThat(response2.getStatusCode()).isNotEqualTo(HttpStatus.CREATED);
    }

    // 3. Kullanıcı getirme: Yetkili (admin) token ile
    @Test
    public void testGetUserByEmail_success() {
        CreatedUserRequest createReq = new CreatedUserRequest();
        createReq.setFirstname("Charlie");
        createReq.setLastname("Chaplin");
        createReq.setEmail("charlie@example.com");
        createReq.setPassword("secret");
        createReq.setPhone("555-0000");
        restTemplate.postForEntity("/users/create", createReq, CreatedUserResponse.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER, ADMIN_TOKEN);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<GetUserResponse> getResponse =
                restTemplate.exchange("/users/get-user/charlie@example.com", HttpMethod.GET, entity, GetUserResponse.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        GetUserResponse user = getResponse.getBody();
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("charlie@example.com");
    }

    // 4. Kullanıcı getirme: Authorization header eksik
    @Test
    public void testGetUserByEmail_missingToken() {
        CreatedUserRequest createReq = new CreatedUserRequest();
        createReq.setFirstname("Dana");
        createReq.setLastname("Scully");
        createReq.setEmail("dana@example.com");
        createReq.setPassword("secret");
        createReq.setPhone("555-1111");
        restTemplate.postForEntity("/users/create", createReq, CreatedUserResponse.class);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange("/users/get-user/dana@example.com", HttpMethod.GET, entity, String.class);

        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }

    // 5. Kullanıcı getirme: Yetkisiz token (non-admin token, ID uyuşmazlığı)
    @Test
    public void testGetUserByEmail_notAuthorized() {
        CreatedUserRequest createReq = new CreatedUserRequest();
        createReq.setFirstname("Eve");
        createReq.setLastname("Polastri");
        createReq.setEmail("eve@example.com");
        createReq.setPassword("secret");
        createReq.setPhone("555-2222");
        restTemplate.postForEntity("/users/create", createReq, CreatedUserResponse.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER, NON_ADMIN_TOKEN);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange("/users/get-user/eve@example.com", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }

    // 6. Kullanıcı güncelleme: Başarılı senaryo (admin token ile)
    @Test
    public void testUpdateUser_success() {
        CreatedUserRequest createReq = new CreatedUserRequest();
        createReq.setFirstname("Frank");
        createReq.setLastname("Sinatra");
        createReq.setEmail("frank@example.com");
        createReq.setPassword("secret");
        createReq.setPhone("555-3333");
        restTemplate.postForEntity("/users/create", createReq, CreatedUserResponse.class);

        UpdateUserRequest updateReq = new UpdateUserRequest();
        updateReq.setFirstname("Franklin");
        updateReq.setLastname("Sinatra");
        updateReq.setEmail("frank@example.com");
        updateReq.setPassword("newsecret");
        updateReq.setPhone("555-4444");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER, ADMIN_TOKEN);
        HttpEntity<UpdateUserRequest> entity = new HttpEntity<>(updateReq, headers);

        ResponseEntity<UpdateUserResponse> response =
                restTemplate.exchange("/users/update-user/frank@example.com", HttpMethod.PUT, entity, UpdateUserResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        UpdateUserResponse updatedUser = response.getBody();
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getFirstname()).isEqualTo("Franklin");
    }

    // 7. Kullanıcı güncelleme: Authorization header eksik
    @Test
    public void testUpdateUser_missingToken() {
        CreatedUserRequest createReq = new CreatedUserRequest();
        createReq.setFirstname("Grace");
        createReq.setLastname("Hopper");
        createReq.setEmail("grace@example.com");
        createReq.setPassword("secret");
        createReq.setPhone("555-5555");
        restTemplate.postForEntity("/users/create", createReq, CreatedUserResponse.class);

        UpdateUserRequest updateReq = new UpdateUserRequest();
        updateReq.setFirstname("Grace");
        updateReq.setLastname("Hopper");
        updateReq.setEmail("grace@example.com");
        updateReq.setPassword("newsecret");
        updateReq.setPhone("555-6666");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UpdateUserRequest> entity = new HttpEntity<>(updateReq, headers);

        ResponseEntity<String> response =
                restTemplate.exchange("/users/update-user/grace@example.com", HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }

    // 8. Kullanıcı güncelleme: Yetkisiz token (non-admin token)
    @Test
    public void testUpdateUser_notAuthorized() {
        CreatedUserRequest createReq = new CreatedUserRequest();
        createReq.setFirstname("Henry");
        createReq.setLastname("Ford");
        createReq.setEmail("henry@example.com");
        createReq.setPassword("secret");
        createReq.setPhone("555-7777");
        restTemplate.postForEntity("/users/create", createReq, CreatedUserResponse.class);

        UpdateUserRequest updateReq = new UpdateUserRequest();
        updateReq.setFirstname("Henry");
        updateReq.setLastname("Ford");
        updateReq.setEmail("henry@example.com");
        updateReq.setPassword("newsecret");
        updateReq.setPhone("555-8888");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER, NON_ADMIN_TOKEN);
        HttpEntity<UpdateUserRequest> entity = new HttpEntity<>(updateReq, headers);

        ResponseEntity<String> response =
                restTemplate.exchange("/users/update-user/henry@example.com", HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }

    // 9. Kullanıcı silme: Başarılı senaryo
    @Test
    public void testDeleteUser_success() {
        CreatedUserRequest createReq = new CreatedUserRequest();
        createReq.setFirstname("Hank");
        createReq.setLastname("Pym");
        createReq.setEmail("hank@example.com");
        createReq.setPassword("secret");
        createReq.setPhone("555-7777");
        restTemplate.postForEntity("/users/create", createReq, CreatedUserResponse.class);

        restTemplate.delete("/users/hank@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER, ADMIN_TOKEN);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange("/users/get-user/hank@example.com", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }

    // 10. Kullanıcı silme: Varolmayan kullanıcı (hata senaryosu)
    @Test
    public void testDeleteUser_nonExisting() {
        HttpHeaders headers = new HttpHeaders();
        // Token gerekli değilse ya da güvenlik filtreleri token'ı kontrol etmiyorsa, header eklemeyebilirsiniz.
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange("/users/nonexistent@example.com", HttpMethod.DELETE, entity, String.class);
        // Burada, varolmayan kullanıcı silinmek istendiğinde 200 OK yerine hata kodu beklenir.
        // Örneğin, uygulamanız UserException'ı 404 Not Found veya 400 Bad Request olarak döndürüyorsa,
        // aşağıdaki kontrol ile doğrulayabilirsiniz:
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }

    // 11. Tüm kullanıcıların getirilmesi: Başarılı senaryo
    @Test
    public void testGetAllUsers_success() {
        CreatedUserRequest user1 = new CreatedUserRequest();
        user1.setFirstname("Ivy");
        user1.setLastname("League");
        user1.setEmail("ivy@example.com");
        user1.setPassword("secret");
        user1.setPhone("555-8888");
        restTemplate.postForEntity("/users/create", user1, CreatedUserResponse.class);

        CreatedUserRequest user2 = new CreatedUserRequest();
        user2.setFirstname("Jack");
        user2.setLastname("Sparrow");
        user2.setEmail("jack@example.com");
        user2.setPassword("secret");
        user2.setPhone("555-9999");
        restTemplate.postForEntity("/users/create", user2, CreatedUserResponse.class);

        ResponseEntity<getAllUserResponse[]> response =
                restTemplate.getForEntity("/users/getAll", getAllUserResponse[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        getAllUserResponse[] users = response.getBody();
        assertThat(users).isNotNull();
        assertThat(users.length).isEqualTo(2);
    }

    // 12. Kafka event gönderimi: Kullanıcı oluşturulduğunda Kafka mesajı gönderiliyor
    @Test
    public void testKafkaEventSentOnUserCreation() throws Exception {
        CreatedUserRequest request = new CreatedUserRequest();
        request.setFirstname("Kate");
        request.setLastname("Bishop");
        request.setEmail("kate@example.com");
        request.setPassword("secret");
        request.setPhone("555-1010");

        restTemplate.postForEntity("/users/create", request, CreatedUserResponse.class);

        if (output != null) {
            Message<byte[]> message = output.receive(5000);
            assertThat(message).isNotNull();

            // Mesaj payload'unu deserialize edip UserCreatedEvent nesnesine dönüştürelim.
            ObjectMapper objectMapper = new ObjectMapper();
            UserCreatedEvent event = objectMapper.readValue(message.getPayload(), UserCreatedEvent.class);
            assertThat(event.getEmail()).isEqualTo("kate@example.com");
        }
    }

    // Dummy JWT Provider: Test ortamında token doğrulamasını basitleştirir.
    @TestConfiguration
    static class DummyJwtTokenProviderConfig {

        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            return new JwtTokenProvider() {
                @Override
                public String createToken(UUID id, String email, Role role) {
                    if (role == Role.ADMIN) {
                        return "dummy-token";
                    } else {
                        return "nonadmin-token";
                    }
                }

                @Override
                public UUID getIdFromToken(String token) {
                    if ("dummy-token".equals(token)) {
                        return UUID.fromString("11111111-1111-1111-1111-111111111111");
                    } else if ("nonadmin-token".equals(token)) {
                        return UUID.fromString("22222222-2222-2222-2222-222222222222");
                    }
                    return null;
                }

                @Override
                public Role getRoleFromToken(String token) {
                    if ("dummy-token".equals(token)) {
                        return Role.ADMIN;
                    } else if ("nonadmin-token".equals(token)) {
                        return Role.CUSTOMER_SERVICE;
                    }
                    return null;
                }
            };
        }
    }
}
