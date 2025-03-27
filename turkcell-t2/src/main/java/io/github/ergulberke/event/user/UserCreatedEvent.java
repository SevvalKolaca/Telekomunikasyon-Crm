package io.github.ergulberke.event.user;

import java.time.LocalDateTime;

public class UserCreatedEvent {
    private String userId;
    private String email;
    private LocalDateTime createdAt;

    public UserCreatedEvent() {}

    public UserCreatedEvent(String userId, String email, LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
    }

    // Getters ve Setters
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
