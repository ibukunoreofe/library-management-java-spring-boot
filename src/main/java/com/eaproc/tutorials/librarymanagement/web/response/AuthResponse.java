package com.eaproc.tutorials.librarymanagement.web.response;

import com.eaproc.tutorials.librarymanagement.web.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    // Getters and setters
    private String token;
    private String tokenType = "Bearer";
    private long expiresIn;
    private String expiresAt;
    private UserDto user;

    public AuthResponse(String token, long expiresIn, String expiresAt, UserDto user) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.user = user;
        this.expiresAt = expiresAt;
    }

}
