package com.eaproc.tutorials.librarymanagement.web.controller.auth;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.RoleEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.service.RoleService;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import com.eaproc.tutorials.librarymanagement.util.JwtTokenUtil;
import com.eaproc.tutorials.librarymanagement.web.mapper.impl.UserMapperImpl;
import com.eaproc.tutorials.librarymanagement.web.request.RegistrationRequest;
import com.eaproc.tutorials.librarymanagement.web.response.AuthResponse;
import com.eaproc.tutorials.librarymanagement.web.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserMapperImpl userMapper;

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSuccessfulRegistration() {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password");

        RoleEntity userRole = new RoleEntity();
        userRole.setName("USER");

        UserEntity savedUser = new UserEntity();
        savedUser.setEmail("test@example.com");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.empty());
        when(roleService.findRoleById(RoleConstants.USER_ROLE_ID)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userService.saveUser(any(UserEntity.class))).thenReturn(savedUser);
        when(jwtTokenUtil.generateToken("test@example.com")).thenReturn("mockToken");
        when(jwtTokenUtil.getExpirationDateFromToken("mockToken")).thenReturn(new java.util.Date());
        when(userMapper.mapTo(savedUser)).thenReturn(new com.eaproc.tutorials.librarymanagement.web.dto.UserDto());

        ResponseEntity<?> response = registerController.registerUser(request, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertEquals("mockToken", authResponse.getToken());
    }

    @Test
    public void testRegistrationWithExistingEmail() {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(new UserEntity()));

        ResponseEntity<?> response = registerController.registerUser(request, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email is already in use", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    public void testRegistrationWithValidationErrors() {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("");
        request.setEmail("");
        request.setPassword("");

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Collections.singletonList(new org.springframework.validation.FieldError("registrationRequest", "name", "Name is required")));

        ResponseEntity<?> response = registerController.registerUser(request, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation errors: Name is required", ((ErrorResponse) response.getBody()).getMessage());
    }
}
