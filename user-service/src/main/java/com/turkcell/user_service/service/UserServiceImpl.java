package com.turkcell.user_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import io.github.ergulberke.jwt.JwtTokenProvider;
import io.github.ergulberke.model.Role;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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

@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public CreatedUserResponse createUser(CreatedUserRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new UserException("User already exists");
        }
        
        User user = modelMapper.map(request, User.class);
        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, CreatedUserResponse.class);
    }

    @Override
    public GetUserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException("User not found with email: " + email));
        return modelMapper.map(user, GetUserResponse.class);
    }

    @Override
    public UpdateUserResponse updateUser(String email, UpdateUserRequest request) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException("User not found with email: " + email));
        
        if(request.getFirstname() != null) {
            user.setFirstname(request.getFirstname());
        }
        if(request.getLastname() != null) {
            user.setLastname(request.getLastname());
        }
        if(request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if(request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }
        if(request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        // Güncelleme anındaki zaman
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UpdateUserResponse.class);
    }

    @Override
    public List<getAllUserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(user -> modelMapper.map(user, getAllUserResponse.class))
            .collect(Collectors.toList());
    }

    @Override
    public DeleteUserResponse deleteUser(String email) {
        if(userRepository.existsByEmail(email)) {
            userRepository.deleteByEmail(email);
            return new DeleteUserResponse("User deleted successfully", LocalDateTime.now());
        }
        throw new UserException("User not found with email: " + email);
    }

    @Override
    public Optional<UUID> getIdByEmail(String email){
        return userRepository.findIdByEmail(email);

    }

    @Override
    public <T> T authorizeAndExecute(String email, String token, Supplier<T> action){
        // Token'dan "Bearer " kısmını kaldır
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Token'dan id ve rol bilgilerini al
        UUID idFromToken = jwtTokenProvider.getIdFromToken(token);
        Role roleFromToken = jwtTokenProvider.getRoleFromToken(token);

        // Email üzerinden kullanıcı id'sini al
        UUID userId = getIdByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with this email: " + email));

        // ADMIN rolü veya token'daki id ile email'den gelen id eşleşiyorsa, istenen işlemi çalıştır.
        if (roleFromToken == Role.ADMIN || idFromToken.equals(userId)) {
            return action.get();
        } else {
            throw new AccessDeniedException("You do not have permission to access this user's information.");
        }
    }
}
