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
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String password;
    private LocalDateTime updatedAt;

    public UpdateUserResponse() {}

    public UpdateUserResponse( String firstname, String lastname, String email, String phone, String password, LocalDateTime updatedAt) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.updatedAt = updatedAt; 
    }

    public UpdateUserResponse(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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