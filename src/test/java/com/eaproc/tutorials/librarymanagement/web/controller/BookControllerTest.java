package com.eaproc.tutorials.librarymanagement.web.controller;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.RoleEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.BookRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.RoleRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import com.eaproc.tutorials.librarymanagement.web.request.auth.AuthenticationRequest;
import com.eaproc.tutorials.librarymanagement.web.request.book.BookRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;

    @BeforeEach
    public void setUp() throws Exception {
        bookRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Create roles
        RoleEntity adminRole = RoleEntity.builder().id (RoleConstants.ADMIN_ROLE_ID).name(RoleConstants.ADMIN_ROLE_NAME).build();
        roleRepository.save(adminRole);

        // Create admin user
        UserEntity adminUser = new UserEntity();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("password"));
        adminUser.setRoleEntity(adminRole);
        adminUser.setName("Admin User");
        userRepository.save(adminUser);

        // Authenticate admin user to get token
        AuthenticationRequest authRequest = new AuthenticationRequest("admin@example.com", "password");
        String authResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        adminToken = objectMapper.readTree(authResponse).get("token").asText();
    }

    @Test
    public void testGetAllBooks() throws Exception {
        BookEntity book1 = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 10, null, null);
        BookEntity book2 = new BookEntity(null, "Title2", "Author2", "0987654321", new Date(), 5, null, null);
        bookRepository.save(book1);
        bookRepository.save(book2);

        mockMvc.perform(get("/api/books")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title1"))
                .andExpect(jsonPath("$[1].title").value("Title2"));
    }

    @Test
    public void testGetBookById() throws Exception {
        BookEntity book = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 10, null, null);
        book = bookRepository.save(book);

        mockMvc.perform(get("/api/books/" + book.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title1"));
    }

    @Test
    public void testGetBookByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/books/1")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    public void testCreateBook() throws Exception {
        BookRequest bookRequest = new BookRequest("Title1", "Author1", "1234567890", LocalDate.of(2023, 1, 1), 10);

        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Title1"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        BookEntity book = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 10, null, null);
        book = bookRepository.save(book);

        BookRequest bookRequest = new BookRequest("Updated Title", "Updated Author", "1234567890", LocalDate.of(2023, 1, 1), 10);

        mockMvc.perform(put("/api/books/" + book.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void testUpdateBookNotFound() throws Exception {
        BookRequest bookRequest = new BookRequest("Updated Title", "Updated Author", "1234567890", LocalDate.of(2023, 1, 1), 10);

        mockMvc.perform(put("/api/books/1")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        BookEntity book = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 10, null, null);
        book = bookRepository.save(book);

        mockMvc.perform(delete("/api/books/" + book.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBookNotFound() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }
}
