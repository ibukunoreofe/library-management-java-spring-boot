package com.eaproc.tutorials.librarymanagement.web.controller.auth;

import com.eaproc.tutorials.librarymanagement.config.annotation.PublicEndpoint;
import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.service.RoleService;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import com.eaproc.tutorials.librarymanagement.util.JwtTokenUtil;
import com.eaproc.tutorials.librarymanagement.util.ValidationUtil;
import com.eaproc.tutorials.librarymanagement.web.mapper.impl.UserMapperImpl;
import com.eaproc.tutorials.librarymanagement.web.request.auth.RegistrationRequest;
import com.eaproc.tutorials.librarymanagement.web.response.AuthResponse;
import com.eaproc.tutorials.librarymanagement.web.response.ErrorResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapperImpl userMapper;

    public RegisterController(UserService userService, RoleService roleService,
                              PasswordEncoder passwordEncoder,
                              JwtTokenUtil jwtTokenUtil,
                              UserMapperImpl userMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
    }

    @PublicEndpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest, BindingResult bindingResult) {
        ResponseEntity<?> validationResponse = ValidationUtil.handleValidationErrors(bindingResult);
        if (validationResponse != null) {
            return validationResponse;
        }

        if (userService.findUserByEmail(registrationRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Email is already in use"));
        }

        UserEntity newUser = UserEntity.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roleEntity(roleService.findRoleById(RoleConstants.USER_ROLE_ID).orElseThrow(() -> new RuntimeException("User role not found")))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuthResponse.create(userService.saveUser(newUser), jwtTokenUtil, userMapper));
    }
}
