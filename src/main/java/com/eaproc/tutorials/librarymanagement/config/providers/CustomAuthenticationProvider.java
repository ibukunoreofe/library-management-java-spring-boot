package com.eaproc.tutorials.librarymanagement.config.providers;

import com.eaproc.tutorials.librarymanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private final UserService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        logger.info("Authenticating user: {}", username);

        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (user == null) {
            logger.error("User not found: {}", username);
            throw new BadCredentialsException("User not found.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.error("Invalid password for user: {}", username);
            throw new BadCredentialsException("Invalid password.");
        }

        logger.info("User authenticated successfully: {}", username);
        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
