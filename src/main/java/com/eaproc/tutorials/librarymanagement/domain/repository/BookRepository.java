package com.eaproc.tutorials.librarymanagement.domain.repository;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Page<BookEntity> findByTitleContainingOrIsbnContaining(String title, String isbn, Pageable pageable);
    List<BookEntity> findByTitleContainingOrIsbnContaining(String title, String isbn);
}
