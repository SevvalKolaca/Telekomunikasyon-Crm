package com.turkcell.user_service.controller;

import java.util.List;
import java.util.UUID;

import io.github.ergulberke.jwt.JwtTokenProvider;
import io.github.ergulberke.model.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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
    @Operation(summary = "Tüm kullanıcıları getir", description = "Admin, müşteri temsilcisi veya teknik destek tarafından çağrılır.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcılar başarıyla listelendi"),
            @ApiResponse(responseCode = "403", description = "Yetki reddedildi"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<getAllUserResponse> getAllUsers(){
        return userService.getAllUsers();
    }


    // Kullanıcıyı email ile getirme ->Admin, Müşteri Temsilcisi, Teknik Destek
    @Operation(summary = "Email adresi ile kullanıcıyı getir", description = "Erişim yetkisi doğrulanır ve kullanıcı bilgisi döner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla bulundu"),
            @ApiResponse(responseCode = "403", description = "Yetki reddedildi"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @GetMapping("/get-user/{email}")
    @ResponseStatus(HttpStatus.OK)
    public GetUserResponse getUserByEmail(@PathVariable String email, @RequestHeader("Authorization") String token) {
        return userService.authorizeAndExecute(email, token, () -> userService.getUserByEmail(email));
    }


    // ADMIN
    @Operation(summary = "Yeni kullanıcı oluştur", description = "Yönetici tarafından yeni kullanıcı kaydı oluşturulur.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kullanıcı başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek verisi (validation hatası)"),
            @ApiResponse(responseCode = "403", description = "Yetki reddedildi (admin değil)"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @PostMapping("/create") //create user
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedUserResponse createUser(@RequestBody @Valid CreatedUserRequest request){
        return userService.createUser(request);
    }

    // Kullanıcı güncelleme -> Admin ve Müşteri Temsilcisi
    @Operation(summary = "Kullanıcı güncelle", description = "Admin veya müşteri temsilcisi erişebilir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla güncellendi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek verisi"),
            @ApiResponse(responseCode = "403", description = "Yetki reddedildi"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @PutMapping("/update-user/{email}") //update user
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserResponse updateUser(@PathVariable String email, @RequestBody UpdateUserRequest request, @RequestHeader("Authorization") String token){
        return userService.authorizeAndExecute(email, token, () -> userService.updateUser(email, request));
    }

    // Kullanıcı silme -> Sadece Admin
    @Operation(summary = "Kullanıcı sil", description = "Sadece admin kullanıcılar erişebilir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla silindi"),
            @ApiResponse(responseCode = "403", description = "Yetki reddedildi"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @DeleteMapping("/{email}") //delete user
    @ResponseStatus(HttpStatus.OK)
    public DeleteUserResponse deleteUser(@PathVariable String email){
        return userService.deleteUser(email);
    }
      


    // CONTROLLER KATMANINDA TEST AMAÇLI KODLAR -----------------------------------------------

    @Operation(summary = "Admin test erişimi", description = "JWT doğrulaması yapılır ve admin rolü kontrol edilir.")
    @ApiResponse(responseCode = "200", description = "JWT geçerli, admin erişimi onaylandı")
    @GetMapping("/admin/test")
    //@PreAuthorize("hasRole('ADMIN')")  // Spring Security "ROLE_" önekini otomatik ekler
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> testAdminAccess() {
        return ResponseEntity.ok("Admin access confirmed. Your JWT is valid.");
    }

    @Operation(summary = "Teknik destek test erişimi", description = "JWT doğrulaması yapılır ve teknik destek rolü kontrol edilir.")
    @ApiResponse(responseCode = "200", description = "JWT geçerli, teknik destek erişimi onaylandı")
    @GetMapping("/tech-help/test")
    //@PreAuthorize("hasRole('ADMIN')")  // Spring Security "ROLE_" önekini otomatik ekler
    @PreAuthorize("hasAuthority('ROLE_TECH_SUPPORT')")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> testTechHelpAccess() {
        return ResponseEntity.ok("Tech Support access confirmed. Your JWT is valid.");
    }

    @Operation(summary = "Müşteri temsilcisi test erişimi", description = "JWT doğrulaması yapılır ve müşteri temsilcisi rolü kontrol edilir.")
    @ApiResponse(responseCode = "200", description = "JWT geçerli, müşteri temsilcisi erişimi onaylandı")
    @GetMapping("/customer-help/test")
    //@PreAuthorize("hasRole('ADMIN')")  // Spring Security "ROLE_" önekini otomatik ekler
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER_SERVICE')")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> testCustHelpAccess() {
        return ResponseEntity.ok("Customer Help access confirmed. Your JWT is valid.");
    }
}
