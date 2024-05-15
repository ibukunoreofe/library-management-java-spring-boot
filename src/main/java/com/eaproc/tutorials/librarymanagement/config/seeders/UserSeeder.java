package com.eaproc.tutorials.librarymanagement.config.seeders;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.RoleEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.service.RoleService;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class UserSeeder {

    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    private final PasswordEncoder passwordEncoder;

    public UserSeeder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Order(2)
    @Transactional
    CommandLineRunner initUsers(UserService userService, RoleService roleService) {
        return args -> {
            RoleEntity adminRoleEntity = roleService.findRoleById(RoleConstants.ADMIN_ROLE_ID).orElse(null);
            if (adminRoleEntity == null) {
                throw new IllegalStateException("Admin role not found");
            }

            UserEntity adminUserEntity = userService.findUserByEmail(adminEmail).orElse(null);

            if (adminUserEntity == null) {
                String encryptedPassword = passwordEncoder.encode("password");

                adminUserEntity = new UserEntity();
                adminUserEntity.setName("Admin"); // You can set a default name or make it configurable
                adminUserEntity.setEmail(adminEmail);
                adminUserEntity.setPassword(encryptedPassword);
                adminUserEntity.setRoleEntity(adminRoleEntity);

                userService.saveUser(adminUserEntity);
            }
        };
    }
}
