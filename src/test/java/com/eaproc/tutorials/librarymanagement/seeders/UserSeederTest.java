package com.eaproc.tutorials.librarymanagement.seeders;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.User;
import com.eaproc.tutorials.librarymanagement.service.RoleService;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserSeederTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Test
    @Transactional
    public void testAdminUserIsPresent() {
        String adminEmail = "admin@example.com"; // Or get it from the property file
        User adminUser = userService.findUserByEmail(adminEmail).orElse(null);

        assertThat(adminUser).isNotNull();
        assertThat(adminUser.getRoleEntity().getId()).isEqualTo(RoleConstants.ADMIN_ROLE_ID);
        assertThat(adminUser.getRoleEntity().getName()).isEqualTo(RoleConstants.ADMIN_ROLE_NAME);
    }
}
