package com.eaproc.tutorials.librarymanagement.config.seeders;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.Role;
import com.eaproc.tutorials.librarymanagement.domain.model.User;
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
            Role adminRole = roleService.findRoleById(RoleConstants.ADMIN_ROLE_ID).orElse(null);
            if (adminRole == null) {
                throw new IllegalStateException("Admin role not found");
            }

            User adminUser = userService.findUserByEmail(adminEmail).orElse(null);

            if (adminUser == null) {
                String encryptedPassword = passwordEncoder.encode("password");

                adminUser = new User();
                adminUser.setName("Admin"); // You can set a default name or make it configurable
                adminUser.setEmail(adminEmail);
                adminUser.setPassword(encryptedPassword);
                adminUser.setRole(adminRole);

                userService.saveUser(adminUser);
            }
        };
    }
}
