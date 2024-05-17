package com.eaproc.tutorials.librarymanagement.service;

import com.eaproc.tutorials.librarymanagement.config.providers.CustomUserDetails;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    private static final Random RANDOM = new SecureRandom();
    private static final int CODE_LENGTH = 6;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading userEntity by email: {}", email);

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("UserEntity not found with email: " + email));

        logger.info("UserEntity found: {}", userEntity);

        CustomUserDetails.CustomUserDetailsBuilder builder = CustomUserDetails.withUsername(email);
        builder.password(userEntity.getPassword());
        builder.roles(userEntity.getRoleEntity().getName());
        CustomUserDetails customUserDetails = builder.build();

        // Extensions
        customUserDetails.setId( userEntity.getId() );
        customUserDetails.setName( userEntity.getName() );

        return customUserDetails;
    }

    public void resetPasswordRequest(String email) throws MessagingException {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            String resetCode = generateResetCode();
            String encodedResetCode = passwordEncoder.encode(resetCode);
            user.setRememberToken(encodedResetCode);
            userRepository.save(user);

            // Send email with reset code
            sendResetEmail(user, resetCode);
        } else {
            throw new RuntimeException("Email not found");
        }
    }

    private String generateResetCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    private void sendResetEmail(UserEntity user, String resetCode) throws MessagingException {
        String subject = "Password Reset Request";
        String body = String.format("Dear %s,%n%nYour password reset code is: %s%n%nPlease use this code to reset your password.%n%nBest regards,%nYour Company",
                user.getName(), resetCode);
        emailService.sendEmail(user.getEmail(), subject, body);
    }
}
