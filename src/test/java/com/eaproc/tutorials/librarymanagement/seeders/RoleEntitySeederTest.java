package com.eaproc.tutorials.librarymanagement.seeders;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.RoleEntity;
import com.eaproc.tutorials.librarymanagement.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoleEntitySeederTest {

    @Autowired
    private RoleService roleService;

    @Test
    @Transactional
    public void testRolesArePresent() {
        RoleEntity adminRoleEntity = roleService.findRoleById(RoleConstants.ADMIN_ROLE_ID).orElse(null);
        RoleEntity userRoleEntity = roleService.findRoleById(RoleConstants.USER_ROLE_ID).orElse(null);

        assertThat(adminRoleEntity).isNotNull();
        assertThat(adminRoleEntity.getName()).isEqualTo(RoleConstants.ADMIN_ROLE_NAME);

        assertThat(userRoleEntity).isNotNull();
        assertThat(userRoleEntity.getName()).isEqualTo(RoleConstants.USER_ROLE_NAME);
    }
}
