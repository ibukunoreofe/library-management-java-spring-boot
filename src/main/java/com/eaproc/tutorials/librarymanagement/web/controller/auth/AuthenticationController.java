package com.eaproc.tutorials.librarymanagement.web.controller.auth;

import com.eaproc.tutorials.librarymanagement.service.UserService;
import com.eaproc.tutorials.librarymanagement.util.JwtTokenUtil;
import com.eaproc.tutorials.librarymanagement.web.request.AuthenticationRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/api/auth/login")
    public String createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("Authentication request for user: {}", authenticationRequest.getEmail());

        try {
            // Validate the username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
            logger.info("Authentication successful for user: {}", authenticationRequest.getEmail());
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", authenticationRequest.getEmail(), e);
            return "Invalid Password! " + e.getMessage();
        }

        // If authentication is successful, generate the JWT token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        return jwtTokenUtil.generateToken(userDetails.getUsername());
    }
}