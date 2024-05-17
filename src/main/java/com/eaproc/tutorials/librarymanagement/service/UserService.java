package com.eaproc.tutorials.librarymanagement.service;

import com.eaproc.tutorials.librarymanagement.config.providers.CustomUserDetails;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import com.eaproc.tutorials.librarymanagement.mail.PasswordResetMail;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public String resetPasswordRequest(String email) throws MessagingException {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            String resetCode = generateResetCode();
            user.setRememberToken(passwordEncoder.encode(resetCode));
            userRepository.save(user);

            // Send email with reset code
            emailService.sendEmail(user.getEmail(), new PasswordResetMail(user.getName(), resetCode));

            return resetCode;
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

    public void resetPassword(String email, String password, String token) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            if (passwordEncoder.matches(token, user.getRememberToken())) {
                user.setPassword(passwordEncoder.encode(password));
                user.setRememberToken(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Email not found");
        }
    }
}
