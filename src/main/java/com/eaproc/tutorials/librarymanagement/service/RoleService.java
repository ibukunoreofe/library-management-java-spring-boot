package com.eaproc.tutorials.librarymanagement.service;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<RoleEntity> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public boolean roleExistsById(Long id) {
        return roleRepository.existsById(id);
    }

    public void saveRole(RoleEntity roleEntity) {
        roleRepository.save(roleEntity);
    }
}
