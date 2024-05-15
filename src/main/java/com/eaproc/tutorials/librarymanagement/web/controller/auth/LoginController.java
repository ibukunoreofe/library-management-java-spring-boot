package com.eaproc.tutorials.librarymanagement.web.controller.auth;

import com.eaproc.tutorials.librarymanagement.annotation.PublicEndpoint;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import com.eaproc.tutorials.librarymanagement.util.JwtTokenUtil;
import com.eaproc.tutorials.librarymanagement.util.ValidationUtil;
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

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PublicEndpoint
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest, BindingResult bindingResult, UserMapperImpl userMapper) {
        ResponseEntity<?> validationResponse = ValidationUtil.handleValidationErrors(bindingResult);
        if (validationResponse != null) {
            return validationResponse;
        }

        UserEntity userEntity;

        try {
            // Validate the username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
            logger.info("Authentication successful for userEntity: {}", authenticationRequest.getEmail());

            // Fetch the userEntity directly
            userEntity = userService.findUserByEmail(authenticationRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

        } catch (AuthenticationException e) {
            logger.error("Authentication failed for userEntity: {}", authenticationRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
        }

        return ResponseEntity.ok(AuthResponse.create(userEntity, jwtTokenUtil, userMapper));
    }
}