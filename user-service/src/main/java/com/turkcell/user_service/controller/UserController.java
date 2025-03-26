package com.turkcell.user_service.controller;

import java.util.List;
import java.util.UUID;

import io.github.ergulberke.jwt.JwtTokenProvider;
import io.github.ergulberke.model.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.turkcell.user_service.dto.create.CreatedUserRequest;
import com.turkcell.user_service.dto.create.CreatedUserResponse;
import com.turkcell.user_service.dto.delete.DeleteUserResponse;
import com.turkcell.user_service.dto.get.GetUserResponse;
import com.turkcell.user_service.dto.getAll.getAllUserResponse;
import com.turkcell.user_service.dto.update.UpdateUserRequest;
import com.turkcell.user_service.dto.update.UpdateUserResponse;
import com.turkcell.user_service.service.UserService;


// TODO: security configde roler düzenlecek!
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

    // Tüm kullanıcıları listeleme -> Admin, Müşteri Temsilcisi, Teknik Destek
    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<getAllUserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    // Kullanıcıyı email ile getirme ->Admin, Müşteri Temsilcisi, Teknik Destek
    @GetMapping("/get-user/{email}")
    @ResponseStatus(HttpStatus.OK)
    public GetUserResponse getUserByEmail(@PathVariable String email, @RequestHeader("Authorization") String token) {
        return userService.authorizeAndExecute(email, token, () -> userService.getUserByEmail(email));
    }

    // ADMIN
    @PostMapping("/create") //create user
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedUserResponse createUser(@RequestBody CreatedUserRequest request){
        return userService.createUser(request);
    }

    // Kullanıcı güncelleme -> Admin ve Müşteri Temsilcisi
    @PutMapping("/update-user/{email}") //update user
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserResponse updateUser(@PathVariable String email, @RequestBody UpdateUserRequest request, @RequestHeader("Authorization") String token){
        return userService.authorizeAndExecute(email, token, () -> userService.updateUser(email, request));
    }

    // Kullanıcı silme -> Sadece Admin
    @DeleteMapping("/{email}") //delete user
    @ResponseStatus(HttpStatus.OK)
    public DeleteUserResponse deleteUser(@PathVariable String email){
        return userService.deleteUser(email);
    }
      


    // CONTROLLER KATMANINDA TEST AMAÇLI KODLAR -----------------------------------------------
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
}
