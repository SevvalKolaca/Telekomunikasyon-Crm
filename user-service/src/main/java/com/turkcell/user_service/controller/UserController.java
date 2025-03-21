package com.turkcell.user_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.user_service.dto.create.CreatedUserRequest;
import com.turkcell.user_service.dto.create.CreatedUserResponse;
import com.turkcell.user_service.dto.delete.DeleteUserResponse;
import com.turkcell.user_service.dto.get.GetUserResponse;
import com.turkcell.user_service.dto.getAll.getAllUserResponse;
import com.turkcell.user_service.dto.update.UpdateUserRequest;
import com.turkcell.user_service.dto.update.UpdateUserResponse;
import com.turkcell.user_service.service.UserService;


// TODO: roller icin endpointler yazılacak!
// TODO: HATALAR DETAYLI ÇIKTILAR DONDURECEK!

@RestController
@RequestMapping("/users")
//@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    @Autowired 
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/admin/test")
    //@PreAuthorize("hasRole('ADMIN')")  // Spring Security "ROLE_" önekini otomatik ekler
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> testAdminAccess() {
        return ResponseEntity.ok("Admin access confirmed. Your JWT is valid.");
    }

    @GetMapping("/tech-help/test")
    //@PreAuthorize("hasRole('ADMIN')")  // Spring Security "ROLE_" önekini otomatik ekler
    @PreAuthorize("hasAuthority('ROLE_TECH_SUPPORT')")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> testTechHelpAccess() {
        return ResponseEntity.ok("Tech Support access confirmed. Your JWT is valid.");
    }

    @GetMapping("/customer-help/test")
    //@PreAuthorize("hasRole('ADMIN')")  // Spring Security "ROLE_" önekini otomatik ekler
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER_SERVICE')")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> testCustHelpAccess() {
        return ResponseEntity.ok("Customer Help access confirmed. Your JWT is valid.");
    }

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<getAllUserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public GetUserResponse getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping //create user 
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedUserResponse createUser(@RequestBody CreatedUserRequest request){
        return userService.createUser(request);
    }

    @PutMapping("/{email}") //update user
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserResponse updateUser(@PathVariable String email, @RequestBody UpdateUserRequest request){
        return userService.updateUser(email, request);
    }

    @DeleteMapping("/{email}") //delete user
    @ResponseStatus(HttpStatus.OK)
    public DeleteUserResponse deleteUser(@PathVariable String email){
        return userService.deleteUser(email);
    }
      

}
