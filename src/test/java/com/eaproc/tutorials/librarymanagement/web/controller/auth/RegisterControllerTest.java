package com.eaproc.tutorials.librarymanagement.web.controller.auth;

import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.domain.model.RoleEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.domain.repository.RoleRepository;
import com.eaproc.tutorials.librarymanagement.domain.repository.UserRepository;
import com.eaproc.tutorials.librarymanagement.web.request.RegistrationRequest;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        // This should not be necessary if seeders runs first
        // however, we have set dirty context here to before class
        userRepository.deleteAll();
        roleRepository.deleteAll();

        RoleEntity userRole = new RoleEntity();
        userRole.setId(RoleConstants.USER_ROLE_ID);
        userRole.setName("USER");
        roleRepository.save(userRole);
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    public void testRegistrationWithExistingEmail() throws Exception {
        // Prepare existing user
        UserEntity existingUser = UserEntity.builder()
                .name("Existing User")
                .email("existing@example.com")
                .password(passwordEncoder.encode("password"))
                .roleEntity(roleRepository.findById(RoleConstants.USER_ROLE_ID).get())
                .build();
        userRepository.save(existingUser);

        RegistrationRequest request = new RegistrationRequest();
        request.setName("Test User");
        request.setEmail("existing@example.com");
        request.setPassword("password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email is already in use"));
    }

    @Test
    public void testRegistrationWithValidationErrors() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("");
        request.setEmail("");
        request.setPassword("");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Name is mandatory")))
                .andExpect(jsonPath("$.message").value(containsString("Email is mandatory")))
                .andExpect(jsonPath("$.message").value(containsString("Password is mandatory")))
                .andExpect(jsonPath("$.message").value(containsString("Password should be between 8 and 50 characters")));
    }
}
