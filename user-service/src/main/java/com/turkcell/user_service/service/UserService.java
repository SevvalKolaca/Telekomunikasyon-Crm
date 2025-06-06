package com.turkcell.user_service.service;
import java.util.function.Supplier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.turkcell.user_service.dto.create.CreatedUserRequest;
import com.turkcell.user_service.dto.create.CreatedUserResponse;
import com.turkcell.user_service.dto.delete.DeleteUserResponse;
import com.turkcell.user_service.dto.get.GetUserResponse;
import com.turkcell.user_service.dto.getAll.getAllUserResponse;
import com.turkcell.user_service.dto.update.UpdateUserRequest;
import com.turkcell.user_service.dto.update.UpdateUserResponse;

public interface UserService {
    CreatedUserResponse createUser(CreatedUserRequest request); 
    GetUserResponse getUserByEmail(String email);
    UpdateUserResponse updateUser(String email, UpdateUserRequest request);
    List<getAllUserResponse> getAllUsers(); 
    DeleteUserResponse deleteUser(String email);
    Optional<UUID> getIdByEmail(String email);
    <T> T authorizeAndExecute(String email, String token, Supplier<T> action);
}
