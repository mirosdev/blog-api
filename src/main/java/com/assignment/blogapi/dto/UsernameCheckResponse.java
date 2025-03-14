package com.assignment.blogapi.dto;

public class UsernameCheckResponse {
    Boolean available;

    public UsernameCheckResponse(Boolean available) {
        this.available = available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}
