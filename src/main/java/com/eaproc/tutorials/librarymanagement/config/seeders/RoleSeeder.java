package com.eaproc.tutorials.librarymanagement.config.seeders;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.RoleEntity;
import com.eaproc.tutorials.librarymanagement.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class RoleSeeder {

    @Bean
    @Order(1)
    @Transactional
    CommandLineRunner initRoles(RoleService roleService) {
        return args -> {
            RoleEntity adminRoleEntity = new RoleEntity(RoleConstants.ADMIN_ROLE_ID, RoleConstants.ADMIN_ROLE_NAME, null, null);
            RoleEntity userRoleEntity = new RoleEntity(RoleConstants.USER_ROLE_ID, RoleConstants.USER_ROLE_NAME, null, null);

            if (!roleService.roleExistsById(RoleConstants.ADMIN_ROLE_ID)) {
                roleService.saveRole(adminRoleEntity);
            }

            if (!roleService.roleExistsById(RoleConstants.USER_ROLE_ID)) {
                roleService.saveRole(userRoleEntity);
            }
        };
    }
}
