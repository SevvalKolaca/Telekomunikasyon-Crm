package com.turkcell.user_service.service;

import java.util.List;
import java.util.UUID;

import com.turkcell.user_service.dto.create.CreatedUserRequest;
import com.turkcell.user_service.dto.create.CreatedUserResponse;
import com.turkcell.user_service.dto.get.GetUserResponse;
import com.turkcell.user_service.dto.update.UpdateUserRequest;
import com.turkcell.user_service.dto.update.UpdateUserResponse;
import com.turkcell.user_service.dto.delete.DeleteUserResponse;
import com.turkcell.user_service.dto.getAll.GetAllUserResponse;

public interface UserService {
    CreatedUserResponse createUser(CreatedUserRequest request); 
    GetUserResponse getUserById(UUID id); 
    UpdateUserResponse updateUser(UUID id, UpdateUserRequest request);
    DeleteUserResponse deleteUser(UUID id); 
    List<GetAllUserResponse> getAllUsers(); 
}
