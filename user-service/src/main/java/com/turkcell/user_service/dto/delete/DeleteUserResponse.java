package com.turkcell.user_service.dto.delete;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;


//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class DeleteUserResponse {
    private String message;
    private LocalDateTime deletedAt;

    public DeleteUserResponse() {}

    public DeleteUserResponse(String message, LocalDateTime deletedAt) {
        this.message = message;
        this.deletedAt = deletedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }   
}