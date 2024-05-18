package com.eaproc.tutorials.librarymanagement.domain.repository;

import com.eaproc.tutorials.librarymanagement.domain.model.CheckoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CheckoutRepository extends JpaRepository<CheckoutEntity, Long> {

    @Query("SELECT COUNT(c) FROM CheckoutEntity c WHERE c.bookEntity.id = :bookId AND c.returnDateTimeUtc IS NULL")
    long countCheckedOutBooks(Long bookId);
}
