package com.eaproc.tutorials.librarymanagement.service;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void saveBook(BookEntity bookEntity) {
        bookRepository.save(bookEntity);
    }

    public long countBooks() {
        return bookRepository.count();
    }


    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<BookEntity> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public BookEntity createBook(BookEntity bookEntity) {
        return bookRepository.save(bookEntity);
    }

    public Optional<BookEntity> updateBook(Long id, BookEntity bookEntity) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(bookEntity.getTitle());
                    existingBook.setAuthor(bookEntity.getAuthor());
                    existingBook.setIsbn(bookEntity.getIsbn());
                    existingBook.setPublishedAt(bookEntity.getPublishedAt());
                    existingBook.setCopies(bookEntity.getCopies());
                    return bookRepository.save(existingBook);
                });
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
