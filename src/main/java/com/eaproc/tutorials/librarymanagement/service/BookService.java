package com.eaproc.tutorials.librarymanagement.service;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void saveBook(BookEntity bookEntity) {
        bookRepository.save(bookEntity);
    }

    public long countBooks() {
        return bookRepository.count();
    }

    public List<BookEntity> findAllBooks() {
        return bookRepository.findAll();
    }
}
