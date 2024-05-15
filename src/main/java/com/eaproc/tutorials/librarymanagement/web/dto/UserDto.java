package com.eaproc.tutorials.librarymanagement.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    // Getters and setters
    private Long id;
    private String name;
    private String email;
    private String role;
    private Long roleId;
    private String createdAt;
}
