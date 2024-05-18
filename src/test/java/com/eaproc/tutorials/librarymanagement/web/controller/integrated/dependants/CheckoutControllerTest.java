package com.eaproc.tutorials.librarymanagement.web.controller;

import com.eaproc.tutorials.librarymanagement.domain.model.*;
import com.eaproc.tutorials.librarymanagement.domain.repository.*;
import com.eaproc.tutorials.librarymanagement.web.request.auth.AuthenticationRequest;
import com.eaproc.tutorials.librarymanagement.web.request.checkout.CheckoutRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CheckoutRepository checkoutRepository;

    private String adminToken;

    @BeforeEach
    public void setUp() throws Exception {
        // Setup admin user and roles
        RoleEntity adminRole = RoleEntity.builder().id(RoleConstants.ADMIN_ROLE_ID).name(RoleConstants.ADMIN_ROLE_NAME).build();
        roleRepository.save(adminRole);

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
    public void testCheckoutBook() throws Exception {
        BookEntity book = new BookEntity(null, "Title1", "Author1", "1234567890", new Date(), 10, null, null);
        book = bookRepository.save(book);

        CheckoutRequest checkoutRequest = new CheckoutRequest(book.getId());

        mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkoutRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Book checked out successfully"));
    }

    @Test
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
