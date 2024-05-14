package com.eaproc.tutorials.librarymanagement.domain.repository;

import com.eaproc.tutorials.librarymanagement.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
