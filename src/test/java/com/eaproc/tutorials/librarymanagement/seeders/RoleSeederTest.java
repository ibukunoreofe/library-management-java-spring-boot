package com.eaproc.tutorials.librarymanagement.seeders;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.Role;
import com.eaproc.tutorials.librarymanagement.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoleSeederTest {

    @Autowired
    private RoleService roleService;

    @Test
    @Transactional
    public void testRolesArePresent() {
        Role adminRole = roleService.findRoleById(RoleConstants.ADMIN_ROLE_ID).orElse(null);
        Role userRole = roleService.findRoleById(RoleConstants.USER_ROLE_ID).orElse(null);

        assertThat(adminRole).isNotNull();
        assertThat(adminRole.getName()).isEqualTo(RoleConstants.ADMIN_ROLE_NAME);

        assertThat(userRole).isNotNull();
        assertThat(userRole.getName()).isEqualTo(RoleConstants.USER_ROLE_NAME);
    }
}
