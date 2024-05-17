package com.eaproc.tutorials.librarymanagement.util;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.RoleEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.RoleRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataManagerUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDataManagerUtil clearUsersAndRoles()
    {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        return this;
    }

    public RoleEntity createUserRole()
    {
        RoleEntity userRole = new RoleEntity();
        userRole.setId(RoleConstants.USER_ROLE_ID);
        userRole.setName(RoleConstants.USER_ROLE_NAME);
        roleRepository.save(userRole);
        return userRole;
    }

    public UserEntity deleteAndCreateSampleUser()
    {
        RoleEntity userRole =
                clearUsersAndRoles()
                .createUserRole();

        UserEntity user = UserEntity.builder()
                .name("Test User")
                .email("test@example.com")
                .password(passwordEncoder.encode("password"))
                .roleEntity(userRole)
                .build();
        userRepository.save(user);
        return user;
    }
}
