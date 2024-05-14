package com.eaproc.tutorials.librarymanagement.domain.repository;

import com.eaproc.tutorials.librarymanagement.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
