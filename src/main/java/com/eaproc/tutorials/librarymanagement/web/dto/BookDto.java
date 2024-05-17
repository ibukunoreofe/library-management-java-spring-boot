package com.eaproc.tutorials.librarymanagement.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publishedAt;
    private Integer copies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
