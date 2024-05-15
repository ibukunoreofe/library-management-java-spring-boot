package com.eaproc.tutorials.librarymanagement.web.controller.auth;

import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import com.eaproc.tutorials.librarymanagement.util.JwtTokenUtil;
import com.eaproc.tutorials.librarymanagement.web.dto.UserDto;
import com.eaproc.tutorials.librarymanagement.web.mapper.impl.UserMapperImpl;
import com.eaproc.tutorials.librarymanagement.web.request.AuthenticationRequest;
import com.eaproc.tutorials.librarymanagement.web.response.AuthResponse;
import com.eaproc.tutorials.librarymanagement.web.response.ErrorResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest, BindingResult bindingResult, UserMapperImpl userMapper) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Validation errors: " + errors));
        }

        logger.info("Authentication request for userEntity: {}", authenticationRequest.getEmail());

        try {
            // Validate the username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
            logger.info("Authentication successful for userEntity: {}", authenticationRequest.getEmail());
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for userEntity: {}", authenticationRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
        }

        // Fetch the userEntity directly
        UserEntity userEntity = userService.findUserByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("UserEntity not found"));

        // Generate the JWT token
        final String token = jwtTokenUtil.generateToken(userEntity.getEmail());
        final Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);

        String expiresAt = DateTimeFormatter.ISO_INSTANT
                .withZone(ZoneOffset.UTC)
                .format(Instant.ofEpochMilli(expiration.getTime()));

        UserDto userResponse = userMapper.mapTo(userEntity);
        AuthResponse authResponse = new AuthResponse(token, expiration.getTime(), expiresAt, userResponse);

        return ResponseEntity.ok(authResponse);
    }
}