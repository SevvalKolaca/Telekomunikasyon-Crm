package com.turkcell.user_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.user_service.dto.create.CreatedUserRequest;
import com.turkcell.user_service.dto.create.CreatedUserResponse;
import com.turkcell.user_service.dto.delete.DeleteUserResponse;
import com.turkcell.user_service.dto.get.GetUserResponse;
import com.turkcell.user_service.dto.getAll.getAllUserResponse;
import com.turkcell.user_service.dto.update.UpdateUserRequest;
import com.turkcell.user_service.dto.update.UpdateUserResponse;
import com.turkcell.user_service.entity.User;
import com.turkcell.user_service.exception.UserException;
import com.turkcell.user_service.repository.UserRepository;
import io.github.ergulberke.event.user.UserCreatedEvent;
import io.github.ergulberke.jwt.JwtTokenProvider;
import io.github.ergulberke.model.Role;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.access.AccessDeniedException;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.modelmapper.ModelMapper modelMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private CreatedUserRequest createUserRequest;

    @BeforeEach
    public void setUp() {
        createUserRequest = new CreatedUserRequest();
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPassword("password");

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
    }

    @Test
    public void testCreateUser_success() {
        // Kullanıcı mevcut değilse:
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(modelMapper.map(createUserRequest, User.class)).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        CreatedUserResponse createdResponse = new CreatedUserResponse();
        createdResponse.setEmail("test@example.com");
        when(modelMapper.map(testUser, CreatedUserResponse.class)).thenReturn(createdResponse);

        // Kafka gönderimi için dummy RecordMetadata ve SendResult oluşturuyoruz.
        RecordMetadata recordMetadata = mock(RecordMetadata.class);
        when(recordMetadata.offset()).thenReturn(1L);
        SendResult<String, UserCreatedEvent> dummySendResult = mock(SendResult.class);
        when(dummySendResult.getRecordMetadata()).thenReturn(recordMetadata);

        // CompletableFuture ile başarılı gönderimi simüle ediyoruz.
        CompletableFuture<SendResult<String, UserCreatedEvent>> future = CompletableFuture.completedFuture(dummySendResult);
        when(kafkaTemplate.send(eq("user-created-topic"), any(UserCreatedEvent.class))).thenReturn(future);

        // Metodu çağır ve sonucu doğrula.
        CreatedUserResponse response = userService.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(kafkaTemplate, times(1)).send(eq("user-created-topic"), any(UserCreatedEvent.class));
    }

    @Test
    public void testCreateUser_userAlreadyExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        assertThrows(UserException.class, () -> userService.createUser(createUserRequest));
        verify(userRepository, never()).save(any(User.class));
        verify(kafkaTemplate, never()).send(anyString(), any(UserCreatedEvent.class));
    }

    @Test
    public void testGetUserByEmail_success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        GetUserResponse getUserResponse = new GetUserResponse();
        getUserResponse.setEmail("test@example.com");
        when(modelMapper.map(testUser, GetUserResponse.class)).thenReturn(getUserResponse);

        GetUserResponse response = userService.getUserByEmail("test@example.com");

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    public void testGetUserByEmail_notFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        assertThrows(UserException.class, () -> userService.getUserByEmail("nonexistent@example.com"));
    }

    @Test
    public void testUpdateUser_success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setFirstname("John");
        updateRequest.setLastname("Doe");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPassword("newpassword");
        updateRequest.setPhone("123456789");

        User updatedUser = new User();
        updatedUser.setId(testUser.getId());
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");
        updatedUser.setFirstname("John");
        updatedUser.setLastname("Doe");
        updatedUser.setPhone("123456789");
        updatedUser.setUpdatedAt(LocalDateTime.now());

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        UpdateUserResponse updateResponse = new UpdateUserResponse();
        updateResponse.setEmail("updated@example.com");
        when(modelMapper.map(updatedUser, UpdateUserResponse.class)).thenReturn(updateResponse);

        UpdateUserResponse response = userService.updateUser("test@example.com", updateRequest);

        assertNotNull(response);
        assertEquals("updated@example.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("another@example.com");

        List<User> userList = List.of(testUser, user2);
        when(userRepository.findAll()).thenReturn(userList);

        getAllUserResponse resp1 = new getAllUserResponse();
        resp1.setEmail("test@example.com");
        getAllUserResponse resp2 = new getAllUserResponse();
        resp2.setEmail("another@example.com");

        when(modelMapper.map(testUser, getAllUserResponse.class)).thenReturn(resp1);
        when(modelMapper.map(user2, getAllUserResponse.class)).thenReturn(resp2);

        List<getAllUserResponse> responses = userService.getAllUsers();
        assertEquals(2, responses.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteUser_success() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        DeleteUserResponse response = userService.deleteUser("test@example.com");

        assertNotNull(response);
        assertTrue(response.getMessage().contains("User deleted successfully"));
        verify(userRepository, times(1)).deleteByEmail("test@example.com");
    }

    @Test
    public void testDeleteUser_userNotFound() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);
        assertThrows(UserException.class, () -> userService.deleteUser("nonexistent@example.com"));
    }

    @Test
    public void testGetIdByEmail_success() {
        UUID expectedId = UUID.randomUUID();
        when(userRepository.findIdByEmail("test@example.com")).thenReturn(Optional.of(expectedId));

        Optional<UUID> result = userService.getIdByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals(expectedId, result.get());
    }

    @Test
    public void testAuthorizeAndExecute_success_adminRole() {
        String token = "Bearer testToken";
        String email = "test@example.com";
        UUID tokenId = UUID.randomUUID();

        when(jwtTokenProvider.getIdFromToken(anyString())).thenReturn(tokenId);
        when(jwtTokenProvider.getRoleFromToken(anyString())).thenReturn(Role.ADMIN);
        // Burada email için tokenId dönmesi sağlanmalı:
        when(userRepository.findIdByEmail("test@example.com")).thenReturn(Optional.of(tokenId));

        Supplier<String> action = () -> "Executed";
        String result = userService.authorizeAndExecute(email, token, action);

        assertEquals("Executed", result);
    }

    @Test
    public void testAuthorizeAndExecute_success_sameUser() {
        String token = "Bearer testToken";
        String email = "test@example.com";
        UUID tokenId = UUID.randomUUID();

        when(jwtTokenProvider.getIdFromToken(anyString())).thenReturn(tokenId);
        // Non-admin rolü olarak CUSTOMER_SERVICE kullanıyoruz.
        when(jwtTokenProvider.getRoleFromToken(anyString())).thenReturn(Role.CUSTOMER_SERVICE);
        when(userRepository.findIdByEmail("test@example.com")).thenReturn(Optional.of(tokenId));

        Supplier<Integer> action = () -> 42;
        Integer result = userService.authorizeAndExecute(email, token, action);

        assertEquals(42, result);
    }

    @Test
    public void testAuthorizeAndExecute_accessDenied() {
        String token = "Bearer testToken";
        String email = "test@example.com";
        UUID tokenId = UUID.randomUUID();

        when(jwtTokenProvider.getIdFromToken(anyString())).thenReturn(tokenId);
        when(jwtTokenProvider.getRoleFromToken(anyString())).thenReturn(Role.CUSTOMER_SERVICE);
        // Farklı bir UUID döndürerek erişim reddini simüle ediyoruz.
        when(userRepository.findIdByEmail("test@example.com")).thenReturn(Optional.of(UUID.randomUUID()));

        Supplier<String> action = () -> "Executed";
        assertThrows(AccessDeniedException.class, () ->
                userService.authorizeAndExecute(email, token, action));
    }

    @Test
    public void testEnumConversion() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        // Enum değeri JSON içinde metin olarak gönderiliyor
        String json = "{\"role\": \"ADMIN\"}";
        Role role = mapper.readValue(json, Role.class);
        assertEquals(Role.ADMIN, role);
    }
}
