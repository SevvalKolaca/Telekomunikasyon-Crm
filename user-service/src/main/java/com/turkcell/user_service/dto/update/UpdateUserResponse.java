package com.turkcell.user_service.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;


//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class UpdateUserResponse {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private LocalDateTime updatedAt;

    public UpdateUserResponse() {}

    public UpdateUserResponse(UUID id, String firstname, String lastname, String email, String phone, LocalDateTime updatedAt) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.updatedAt = updatedAt; 
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }       

    public String getFirstname() {
        return firstname;
    }   

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }              

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }   

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

}