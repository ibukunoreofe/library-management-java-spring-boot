package com.eaproc.tutorials.librarymanagement.web.controller.integrated.dependants;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.CheckoutEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.BookRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.CheckoutRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import com.eaproc.tutorials.librarymanagement.web.controller.integrated.CreateAndEnsureAdminLoginTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReturnCheckoutControllerTest {

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
    private static Long checkoutId;

    @BeforeEach
    public void setUp() throws Exception {
        adminToken = CreateAndEnsureAdminLoginTest.getAdminToken();
    }

    @Test
    @Order(1)
    public void testReturnBookNotFound() throws Exception {
        mockMvc.perform(patch("/api/checkouts/999")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Checkout not found"));
    }

    @Test
    @Order(2)
    public void testReturnBookSuccessful() throws Exception {

        checkoutRepository.deleteAll(); // it seems at this point seeders are always running, meaning there will be books in library
        bookRepository.deleteAll(); // it seems at this point seeders are always running, meaning there will be books in library

        // Create book and checkout entry
        BookEntity book = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 10, null, null);
        book = bookRepository.save(book);

        UserEntity adminUser = userRepository.findByEmail("admin@example.com").orElseThrow();

        CheckoutEntity checkoutEntity = new CheckoutEntity();
        checkoutEntity.setBookEntity(book);
        checkoutEntity.setUserEntity(adminUser);
        checkoutEntity.setCheckoutDateTimeUtc(LocalDateTime.now());
        checkoutEntity.setReturnDateTimeUtc(null);
        checkoutEntity = checkoutRepository.save(checkoutEntity);
        checkoutId = checkoutEntity.getId();

        mockMvc.perform(patch("/api/checkouts/" + checkoutId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkoutDateTimeUtc").exists())
                .andExpect(jsonPath("$.returnDateTimeUtc").isNotEmpty());
    }

    @Test
    @Order(3)
    public void testReturnBookThatIsAlreadyReturned() throws Exception {
        // Try returning the book again
        mockMvc.perform(patch("/api/checkouts/" + checkoutId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Book already returned"));
    }
}
