package com.eaproc.tutorials.librarymanagement.web.controller.integrated;

import com.eaproc.tutorials.librarymanagement.web.request.auth.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateAndEnsureAdminLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${admin.password:password}")
    private String adminPassword;

    private static String adminToken;

    public static String getAdminToken() {
        return adminToken;
    }

    @Test
    public void createAndLoginAdmin() throws Exception {

        // this is required because even when DirtiesContext, seeders still run first
        // userRepository.deleteAll();
        // roleRepository.deleteAll();

        // RoleEntity adminRole = userDataManagerUtil.createAdminRole();

        // Option 2 - Since I believe it will be available
        // RoleEntity adminRole = roleRepository.findById(RoleConstants.ADMIN_ROLE_ID).get();

        // Creating admin
        // UserEntity adminUser = new UserEntity();
        // adminUser.setEmail("admin@example.com");
        // adminUser.setPassword(passwordEncoder.encode("password"));
        // adminUser.setRoleEntity(adminRole);
        // adminUser.setName("Admin User");
        // userRepository.save(adminUser);

        // Authenticate admin user to get token
        AuthenticationRequest authRequest = new AuthenticationRequest(adminEmail, adminPassword);
        MvcResult authResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        adminToken = objectMapper.readTree(authResponse.getResponse().getContentAsString()).get("token").asText();
    }
}
