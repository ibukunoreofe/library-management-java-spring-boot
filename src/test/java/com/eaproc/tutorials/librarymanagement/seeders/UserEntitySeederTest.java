package com.eaproc.tutorials.librarymanagement.seeders;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserEntitySeederTest {

    @Autowired
    private UserService userService;

    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    @Test
    @Transactional
    public void testAdminUserIsPresent() {
        UserEntity adminUserEntity = userService.findUserByEmail(adminEmail).orElse(null);

        assertThat(adminUserEntity).isNotNull();
        assertThat(adminUserEntity.getRoleEntity().getId()).isEqualTo(RoleConstants.ADMIN_ROLE_ID);
        assertThat(adminUserEntity.getRoleEntity().getName()).isEqualTo(RoleConstants.ADMIN_ROLE_NAME);
    }
}
