package com.eaproc.tutorials.librarymanagement.seeders;

import com.eaproc.tutorials.librarymanagement.config.seeders.BookSeeder;
import com.eaproc.tutorials.librarymanagement.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BookEntitySeederTest {

    @Autowired
    private BookService bookService;

    @Test
    @Transactional
    public void testAtLeastTenBooksArePresent() {
        long bookCount = bookService.countBooks();
        assertThat(bookCount).isGreaterThanOrEqualTo(BookSeeder.MINIMUM_AVAILABLE_BOOKS_AT_ALL_TIMES);
    }
}
