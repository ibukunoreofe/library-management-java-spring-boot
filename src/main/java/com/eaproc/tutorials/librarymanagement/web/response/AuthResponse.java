package com.eaproc.tutorials.librarymanagement.web.response;

import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.util.JwtTokenUtil;
import com.eaproc.tutorials.librarymanagement.web.dto.UserDto;
import com.eaproc.tutorials.librarymanagement.web.mapper.Mapper;
import com.eaproc.tutorials.librarymanagement.web.mapper.impl.UserMapperImpl;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    // Static method to create AuthResponse
    public static AuthResponse create(UserEntity userEntity, JwtTokenUtil jwtTokenUtil, Mapper<UserEntity,UserDto> userMapper) {
        // Generate the JWT token
        final String token = jwtTokenUtil.generateToken(userEntity.getEmail());
        final Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);

        String expiresAt = DateTimeFormatter.ISO_INSTANT
                .withZone(ZoneOffset.UTC)
                .format(Instant.ofEpochMilli(expiration.getTime()));

        UserDto userResponse = userMapper.mapTo(userEntity);
        return new AuthResponse(token, expiration.getTime(), expiresAt, userResponse);
    }
}
