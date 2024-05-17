package com.eaproc.tutorials.librarymanagement.web.controller.auth;

import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import com.eaproc.tutorials.librarymanagement.util.UserDataManagerUtil;
import com.eaproc.tutorials.librarymanagement.web.request.auth.PasswordResetConfirmRequest;
import com.eaproc.tutorials.librarymanagement.web.request.auth.PasswordResetRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class PasswordResetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDataManagerUtil userDataManagerUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUpOnce() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        userDataManagerUtil.deleteAndCreateSampleUser();
    }

    @Test
    public void testResetPasswordRequestWithInvalidEmailFormat() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("invalid-email");

        mockMvc.perform(post("/api/auth/password/reset-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation errors: must be a well-formed email address"));
    }

    @Test
    public void testResetPasswordRequestWithMissingEmail() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest();

        mockMvc.perform(post("/api/auth/password/reset-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation errors: must not be blank"));
    }

    @Test
    public void testResetPasswordRequestWithNonExistentEmail() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("nonexistent@example.com");

        mockMvc.perform(post("/api/auth/password/reset-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email not found"));
    }

    @Test
    public void testSuccessfulResetPasswordRequest() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("test@example.com");

        mockMvc.perform(post("/api/auth/password/reset-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password reset code sent to your email."));
    }

    @Test
    public void testResetPasswordWithInvalidEmailFormat() throws Exception {
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
        request.setEmail("invalid-email");
        request.setPassword("newpassword");
        request.setPasswordConfirmation("newpassword");
        request.setToken("123456");

        mockMvc.perform(post("/api/auth/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation errors: must be a well-formed email address"));
    }

    @Test
    public void testResetPasswordWithMissingFields() throws Exception {
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();

        mockMvc.perform(post("/api/auth/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.allOf(
                        org.hamcrest.Matchers.containsString("Email is mandatory"),
                        org.hamcrest.Matchers.containsString("Password is mandatory"),
                        org.hamcrest.Matchers.containsString("Password confirmation is mandatory"),
                        org.hamcrest.Matchers.containsString("Token is mandatory")
                )));
    }

    @Test
    public void testResetPasswordWithMismatchedPasswords() throws Exception {
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
        request.setEmail("test@example.com");
        request.setPassword("newpassword");
        request.setPasswordConfirmation("differentpassword");
        request.setToken("123456");

        mockMvc.perform(post("/api/auth/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Passwords do not match"));
    }

    @Test
    public void testResetPasswordWithInvalidToken() throws Exception {
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
        request.setEmail("test@example.com");
        request.setPassword("newpassword");
        request.setPasswordConfirmation("newpassword");
        request.setToken("000000");

        mockMvc.perform(post("/api/auth/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid token"));
    }

    @Test
    public void testSuccessfulPasswordReset() throws Exception {
        // Mock the reset request first to set the token
        String token = userService.resetPasswordRequest("test@example.com"); // Use the actual token from the email sent in a real scenario

        // Fetch the user and get the token from the repository
        UserEntity user = userRepository.findByEmail("test@example.com").orElseThrow();

        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
        request.setEmail("test@example.com");
        request.setPassword("newpassword");
        request.setPasswordConfirmation("newpassword");
        request.setToken(token);

        mockMvc.perform(post("/api/auth/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Your password has been reset."));
    }
}
