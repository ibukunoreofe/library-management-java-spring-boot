package com.eaproc.tutorials.librarymanagement.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@lombok.Setter
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class PasswordResetRequest {
    @NotBlank
    @Email
    private String email;
}
