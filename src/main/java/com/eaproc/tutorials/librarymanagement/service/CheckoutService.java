package com.eaproc.tutorials.librarymanagement.service;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.CheckoutEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.BookRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.CheckoutRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class CheckoutService {

    private final BookRepository bookRepository;
    private final CheckoutRepository checkoutRepository;
    private final UserRepository userRepository;

    public CheckoutService(BookRepository bookRepository, CheckoutRepository checkoutRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CheckoutEntity checkoutBook(Long bookId, String username) {
        Optional<BookEntity> bookEntityOptional = bookRepository.findById(bookId);
        if (bookEntityOptional.isEmpty()) {
            throw new RuntimeException("Book not found");
        }

        BookEntity bookEntity = bookEntityOptional.get();
        long checkedOutBooks = checkoutRepository.countCheckedOutBooks(bookId);

        if (checkedOutBooks >= bookEntity.getCopies()) {
            throw new RuntimeException("No copies of the book are available for checkout");
        }

        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CheckoutEntity checkoutEntity = new CheckoutEntity();
        checkoutEntity.setBookEntity(bookEntity);
        checkoutEntity.setUserEntity(userEntity);
        return checkoutRepository.save(checkoutEntity);
    }

    @Transactional
    public CheckoutEntity returnBook(Long checkoutId) {
        Optional<CheckoutEntity> checkoutEntityOptional = checkoutRepository.findById(checkoutId);
        if (checkoutEntityOptional.isEmpty()) {
            throw new RuntimeException("Checkout not found");
        }

        CheckoutEntity checkoutEntity = checkoutEntityOptional.get();
        if (checkoutEntity.getReturnDateTimeUtc() != null) {
            throw new RuntimeException("Book already returned");
        }

        checkoutEntity.setReturnDateTimeUtc(LocalDateTime.now(ZoneOffset.UTC));
        return checkoutRepository.save(checkoutEntity);
    }

    public int countCheckedOutBooks(Long bookId) {
        return (int) checkoutRepository.countCheckedOutBooks(bookId);
    }

    public int countTimesCheckedOutAndReturned(Long bookId) {
        return checkoutRepository.countByBookIdAndReturnDateTimeUtcIsNotNull(bookId);
    }
}
