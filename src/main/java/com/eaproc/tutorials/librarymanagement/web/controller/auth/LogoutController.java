package com.eaproc.tutorials.librarymanagement.web.controller.auth;

import com.eaproc.tutorials.librarymanagement.web.response.LogoutResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LogoutController {

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Respond with a success message including the username
        return ResponseEntity.ok(
                LogoutResponse
                        .builder()
                        .message(
                                String.format("User '%s' has been logged out successfully", user.getUsername())
                        )
                        .build()
        );
    }
}
