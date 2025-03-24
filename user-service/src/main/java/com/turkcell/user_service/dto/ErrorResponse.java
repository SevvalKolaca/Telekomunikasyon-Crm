package com.turkcell.user_service.dto;

import java.time.LocalDateTime;


public class ErrorResponse {
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse() {}

    public ErrorResponse(int statusCode, String message, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }   

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) { 
        this.timestamp = timestamp;
    }   
} 