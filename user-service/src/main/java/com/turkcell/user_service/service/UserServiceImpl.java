package com.turkcell.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;
import com.turkcell.user_service.dto.create.CreatedUserRequest;
import com.turkcell.user_service.dto.create.CreatedUserResponse;
import com.turkcell.user_service.dto.get.GetUserResponse;
import com.turkcell.user_service.dto.update.UpdateUserResponse;
import com.turkcell.user_service.dto.update.UpdateUserRequest;
import com.turkcell.user_service.dto.delete.DeleteUserResponse;
import com.turkcell.user_service.dto.getAll.GetAllUserResponse;
import com.turkcell.user_service.entity.User;
import com.turkcell.user_service.exception.UserException;
import com.turkcell.user_service.repository.UserRepository;
import com.turkcell.user_service.service.UserService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
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
    public GetUserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserException("User not found with id: " + id));
        return modelMapper.map(user, GetUserResponse.class);
    }

    @Override
    public UpdateUserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserException("User not found with id: " + id));
        
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
        
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UpdateUserResponse.class);
    }

    @Override 
    public List<GetAllUserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, GetAllUserResponse.class)).collect(Collectors.toList());
    }

    @Override
    public DeleteUserResponse deleteUser(UUID id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new DeleteUserResponse("User deleted successfully", LocalDateTime.now());
        }
        throw new UserException("User not found with id: " + id);
    }    
}

