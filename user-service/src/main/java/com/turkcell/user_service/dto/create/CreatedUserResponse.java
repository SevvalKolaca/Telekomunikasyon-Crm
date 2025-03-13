package com.turkcell.user_service.dto.create;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreatedUserResponse {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private LocalDateTime createdAt;

    // Boş constructor - ModelMapper için gerekli
    public CreatedUserResponse() {
    }

    // Tüm alanları içeren constructor
    public CreatedUserResponse(UUID id, String firstname, String lastname, String email, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    // Getter ve Setter metodları
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}