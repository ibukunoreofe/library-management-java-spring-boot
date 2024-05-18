package com.eaproc.tutorials.librarymanagement.web.controller.integrated.dependants;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.CheckoutEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.BookRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.CheckoutRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import com.eaproc.tutorials.librarymanagement.web.controller.integrated.CreateAndEnsureAdminLoginTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    private String adminToken;

    @BeforeEach
    public void setUp() {
        adminToken = CreateAndEnsureAdminLoginTest.getAdminToken();

        checkoutRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    public void testGetAllBooksPaginated() throws Exception {

        BookEntity book1 = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 10, null, null);
        BookEntity book2 = new BookEntity(null, "Title2", "Author2", "0987654321", new Date(), 5, null, null);
        bookRepository.save(book1);
        bookRepository.save(book2);

        mockMvc.perform(get("/api/reports/books?paginate=true&page=0&size=10")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Title1"))
                .andExpect(jsonPath("$.content[1].title").value("Title2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    public void testGetAllBooksSearch() throws Exception {

        BookEntity book1 = new BookEntity(null, "Java Programming", "Author1", "1234567890", new Date(), 10, null, null);
        BookEntity book2 = new BookEntity(null, "Spring Framework", "Author2", "0987654321", new Date(), 5, null, null);
        bookRepository.save(book1);
        bookRepository.save(book2);

        mockMvc.perform(get("/api/reports/books?search=Java")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Java Programming"))
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    public void testGetBooksPendingReturn() throws Exception {

        BookEntity book = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 10, null, null);
        book = bookRepository.save(book);

        UserEntity adminUser = userRepository.findByEmail("admin@example.com").orElseThrow();

        CheckoutEntity checkoutEntity = new CheckoutEntity();
        checkoutEntity.setBookEntity(book);
        checkoutEntity.setUserEntity(adminUser);
        checkoutEntity.setCheckoutDateTimeUtc(LocalDateTime.now());
        checkoutEntity.setReturnDateTimeUtc(null);
        checkoutRepository.save(checkoutEntity);

        mockMvc.perform(get("/api/reports/books/pending-return")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Title1"))
                .andExpect(jsonPath("$.content[0].checkedOutCount").value(1))
                .andExpect(jsonPath("$.content[0].quantityLeft").value(9));
    }

    @Test
    public void testGetBooksReturned() throws Exception {
        BookEntity book = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 10, null, null);
        book = bookRepository.save(book);

        UserEntity adminUser = userRepository.findByEmail("admin@example.com").orElseThrow();

        CheckoutEntity checkoutEntity = new CheckoutEntity();
        checkoutEntity.setBookEntity(book);
        checkoutEntity.setUserEntity(adminUser);
        checkoutEntity.setCheckoutDateTimeUtc(LocalDateTime.now());
        checkoutEntity.setReturnDateTimeUtc(LocalDateTime.now());
        checkoutRepository.save(checkoutEntity);

        mockMvc.perform(get("/api/reports/books/returned")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Title1"))
                .andExpect(jsonPath("$.content[0].timesCheckedOutAndReturned").value(1));
    }

    @Test
    public void testGetBooksReturnedEmpty() throws Exception {
        mockMvc.perform(get("/api/reports/books/returned")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}
