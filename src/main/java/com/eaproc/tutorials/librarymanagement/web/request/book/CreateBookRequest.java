package com.eaproc.tutorials.librarymanagement.web.request.book;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(min = 3, max = 255, message = "Author must be between 3 and 255 characters")
    private String author;

    @NotBlank(message = "ISBN is mandatory")
    @Pattern(regexp = "\\d{10}", message = "ISBN must be exactly 10 digits")
    private String isbn;

    @NotNull(message = "Published date is mandatory")
    @Past(message = "Published date must be in the past")
    private LocalDate publishedAt;

    @NotNull(message = "Copies is mandatory")
    @Min(value = 1, message = "Copies must be at least 1")
    @Max(value = 20000, message = "Copies must not exceed 20000")
    private Integer copies;
}
