package com.eaproc.tutorials.librarymanagement.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordResetConfirmRequest {

    @Email(message = "must be a well-formed email address")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 50, message = "Password should be between 8 and 50 characters")
    private String password;

    @NotBlank(message = "Password confirmation is mandatory")
    private String passwordConfirmation;

    @NotBlank(message = "Token is mandatory")
    @Pattern(regexp = "\\d{6}", message = "Token must be a 6-digit number")
    private String token;
}
