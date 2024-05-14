package com.eaproc.tutorials.librarymanagement.config;

import com.eaproc.tutorials.librarymanagement.domain.model.Role;
import com.eaproc.tutorials.librarymanagement.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class RoleSeeder {

    @Bean
    @Transactional
    CommandLineRunner initRoles(RoleService roleService) {
        return args -> {
            Role adminRole = new Role(RoleConstants.ADMIN_ROLE_ID, RoleConstants.ADMIN_ROLE_NAME, null, null);
            Role userRole = new Role(RoleConstants.USER_ROLE_ID, RoleConstants.USER_ROLE_NAME, null, null);

            if (!roleService.roleExistsById(RoleConstants.ADMIN_ROLE_ID)) {
                roleService.saveRole(adminRole);
            }

            if (!roleService.roleExistsById(RoleConstants.USER_ROLE_ID)) {
                roleService.saveRole(userRole);
            }
        };
    }
}
