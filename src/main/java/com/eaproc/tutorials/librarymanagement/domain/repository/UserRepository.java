package com.eaproc.tutorials.librarymanagement.domain.repository;

import com.eaproc.tutorials.librarymanagement.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
