package com.eaproc.tutorials.librarymanagement.web.controller.auth;

import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import com.eaproc.tutorials.librarymanagement.util.JwtTokenUtil;
import com.eaproc.tutorials.librarymanagement.web.mapper.impl.UserMapperImpl;
import com.eaproc.tutorials.librarymanagement.web.request.auth.AuthenticationRequest;
import com.eaproc.tutorials.librarymanagement.web.response.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserService userService;

    @Mock
    private UserMapperImpl userMapper;

    @InjectMocks
    private LoginController loginController;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSuccessfulLogin() {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        when(jwtTokenUtil.generateToken("test@example.com")).thenReturn("mockToken");
        when(jwtTokenUtil.getExpirationDateFromToken("mockToken")).thenReturn(new java.util.Date());
        when(userMapper.mapTo(userEntity)).thenReturn(new com.eaproc.tutorials.librarymanagement.web.dto.UserDto());

        ResponseEntity<?> response = loginController.createAuthenticationToken(request, bindingResult, userMapper);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assert authResponse != null;
        assertEquals("mockToken", authResponse.getToken());
    }

    @Test
    public void testFailedLogin() {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Invalid password"));

        ResponseEntity<?> response = loginController.createAuthenticationToken(request, bindingResult, userMapper);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid password", ((com.eaproc.tutorials.librarymanagement.web.response.ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    public void testValidationErrors() {
        AuthenticationRequest request = new AuthenticationRequest("", "");

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Collections.singletonList(new org.springframework.validation.FieldError("authenticationRequest", "email", "Email is required")));

        ResponseEntity<?> response = loginController.createAuthenticationToken(request, bindingResult, userMapper);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation errors: Email is required", ((com.eaproc.tutorials.librarymanagement.web.response.ErrorResponse) response.getBody()).getMessage());
    }
}
