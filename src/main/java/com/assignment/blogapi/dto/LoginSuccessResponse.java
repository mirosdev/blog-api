package com.assignment.blogapi.dto;

public class LoginSuccessResponse {
    private String token;

    public LoginSuccessResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
