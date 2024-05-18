package com.eaproc.tutorials.librarymanagement.service;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.BookRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.CheckoutRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BookService {

    private final BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
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

    public Page<BookEntity> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Optional<BookEntity> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Page<BookEntity> searchBooks(String searchValue, Pageable pageable) {
        return bookRepository.findByTitleContainingOrIsbnContaining(searchValue, searchValue, pageable);
    }

    public List<BookEntity> searchBooks(String searchValue) {
        return bookRepository.findByTitleContainingOrIsbnContaining(searchValue, searchValue);
    }

    public BookEntity createBook(BookEntity bookEntity) {
        return bookRepository.save(bookEntity);
    }

    public Optional<BookEntity> updateBook(Long id, BookEntity updatedBook) {
        long checkedOutBooks = checkoutRepository.countCheckedOutBooks(id);
        if (updatedBook.getCopies() < checkedOutBooks) {
            throw new IllegalArgumentException("Number of copies cannot be less than the number of books currently checked out which is %d.".formatted(checkedOutBooks));
        }

        Optional<BookEntity> existingBookOpt = bookRepository.findById(id);
        if (existingBookOpt.isPresent()) {
            BookEntity existingBook = existingBookOpt.get();
            updatedBook.setId(id);
            updatedBook.setCreatedAt(existingBook.getCreatedAt());  // Ensure createdAt is preserved
            return Optional.of(bookRepository.save(updatedBook));
        } else {
            return Optional.empty();
        }
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
