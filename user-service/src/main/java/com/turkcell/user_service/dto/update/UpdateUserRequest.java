package com.turkcell.user_service.dto.update;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private LocalDateTime updatedAt;

    //public UpdateUserRequest(String firstname, String lastname, String email, String password, String phone, LocalDateTime updatedAt) {
    //    this.firstname = firstname;
    //    this.lastname = lastname;
    //    this.email = email;
    //    this.password = password;
    //    this.phone = phone;
    //    this.updatedAt = updatedAt; 
    //}

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