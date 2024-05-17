package com.eaproc.tutorials.librarymanagement.web.controller.auth;

import com.eaproc.tutorials.librarymanagement.web.response.LogoutResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.eaproc.tutorials.librarymanagement.config.providers.CustomAuthenticationProvider.auth;

@RestController
@RequestMapping("/api/auth")
public class LogoutController {

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout() {
        // Respond with a success message including the username
        return ResponseEntity.ok(
                LogoutResponse
                        .builder()
                        .userId(auth().getId())
                        .message(
                                String.format("User '%s' has been logged out successfully", auth().getUsername())
                        )
                        .build()
        );
    }
}
