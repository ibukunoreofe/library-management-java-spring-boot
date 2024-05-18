package com.eaproc.tutorials.librarymanagement.web.controller.integrated.dependants;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.CheckoutEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.BookRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.CheckoutRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import com.eaproc.tutorials.librarymanagement.web.controller.integrated.CreateAndEnsureAdminLoginTest;
import com.eaproc.tutorials.librarymanagement.web.request.checkout.CheckoutRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CheckoutControllerTest {

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
    private Long checkoutId;

    @BeforeEach
    public void setUp() throws Exception {
        adminToken = CreateAndEnsureAdminLoginTest.getAdminToken();
        checkoutRepository.deleteAll(); // it seems at this point seeders are always running, meaning there will be books in library
        bookRepository.deleteAll(); // it seems at this point seeders are always running, meaning there will be books in library
    }

    @Test
    @Order(1)
    public void testCheckoutBook() throws Exception {
        BookEntity book = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 10, null, null);
        book = bookRepository.save(book);

        CheckoutRequest checkoutRequest = new CheckoutRequest(book.getId());

        mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkoutRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookTitle").value("Title1"))
                .andExpect(jsonPath("$.returnDateTimeUtc").doesNotExist());
    }

    @Test
    @Order(2)
    public void testCheckoutBookNotFound() throws Exception {
        CheckoutRequest checkoutRequest = new CheckoutRequest(999L);

        mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkoutRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    @Order(3)
    public void testCheckoutBookNoCopiesAvailable() throws Exception {
        BookEntity book = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 1, null, null);
        book = bookRepository.save(book);

        UserEntity user = userRepository.findByEmail("admin@example.com").orElseThrow();
        CheckoutEntity checkoutEntity = new CheckoutEntity();
        checkoutEntity.setBookEntity(book);
        checkoutEntity.setUserEntity(user);
        checkoutRepository.save(checkoutEntity);

        CheckoutRequest checkoutRequest = new CheckoutRequest(book.getId());

        mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkoutRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No copies of the book are available for checkout"));
    }

}
