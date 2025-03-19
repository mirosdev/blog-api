package com.assignment.blogapi.dto;

public class AuthenticationError {
    private String message;

    public AuthenticationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
