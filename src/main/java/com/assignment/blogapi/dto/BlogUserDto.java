package com.assignment.blogapi.dto;

import java.util.UUID;

public class BlogUserDto {
    private UUID uuid;
    private String email;

    public BlogUserDto(UUID uuid, String email) {
        this.uuid = uuid;
        this.email = email;
    }
}
