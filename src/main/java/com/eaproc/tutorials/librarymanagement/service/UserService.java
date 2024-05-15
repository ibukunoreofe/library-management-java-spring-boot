package com.eaproc.tutorials.librarymanagement.service;

import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading userEntity by email: {}", email);

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("UserEntity not found with email: " + email));

        logger.info("UserEntity found: {}", userEntity);

        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(email);
        builder.password(userEntity.getPassword());
        builder.roles(userEntity.getRoleEntity().getName());
        return builder.build();
    }
}
