package com.turkcell.user_service.dto.update;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;


//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class UpdateUserRequest {
    private String firstname;
    private String lastname;
    private String password;
    private String phone;
    private String email;
    private LocalDateTime updatedAt;

    public UpdateUserRequest(String firstname, String lastname, String password, String phone, String email, LocalDateTime updatedAt) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.phone = phone;
        this.email = email;
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
    

}