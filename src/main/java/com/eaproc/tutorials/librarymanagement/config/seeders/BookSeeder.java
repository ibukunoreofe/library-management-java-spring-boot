package com.eaproc.tutorials.librarymanagement.config.seeders;

import com.eaproc.tutorials.librarymanagement.domain.model.Book;
import com.eaproc.tutorials.librarymanagement.service.BookService;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@Configuration
public class BookSeeder {

    private static final int MIN_COPIES = 30;
    private static final int MAX_COPIES = 100;
    private static final int MIN_PUBLISH_YEAR_OFFSET = 5;
    private static final int MAX_PUBLISH_YEAR_OFFSET = 20;
    public static final int MINIMUM_AVAILABLE_BOOKS_AT_ALL_TIMES = 20;

    @Bean
    @Order(3)
    @Transactional
    CommandLineRunner initBooks(BookService bookService) {
        return args -> {
            int booksCount = (int) bookService.countBooks();
            if (booksCount < MINIMUM_AVAILABLE_BOOKS_AT_ALL_TIMES) {
                Faker faker = new Faker();
                Random random = new Random();

                for (int i = booksCount; i < MINIMUM_AVAILABLE_BOOKS_AT_ALL_TIMES; i++) {
                    String title = faker.book().title();
                    String author = faker.book().author();
                    String isbn = faker.code().isbn10(); // Generating ISBN-10 format
                    Date publishDate = faker.date().between(
                            Date.from(LocalDate.now().minusYears(MAX_PUBLISH_YEAR_OFFSET).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(LocalDate.now().minusYears(MIN_PUBLISH_YEAR_OFFSET).atStartOfDay(ZoneId.systemDefault()).toInstant())
                    );
                    int copies = MIN_COPIES + random.nextInt(MAX_COPIES - MIN_COPIES + 1);

                    Book book = new Book();
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setIsbn(isbn);
                    book.setPublishedAt(publishDate);
                    book.setCopies(copies);

                    bookService.saveBook(book);
                }
            }
        };
    }
}
