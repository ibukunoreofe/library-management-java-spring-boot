package com.eaproc.tutorials.librarymanagement.web.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
