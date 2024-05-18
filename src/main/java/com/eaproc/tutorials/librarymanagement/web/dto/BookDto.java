package com.eaproc.tutorials.librarymanagement.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String publishedAt;
    private Integer copies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setPublishedAt(Date publishedAt) {
        // Create a SimpleDateFormat instance with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.publishedAt = dateFormat.format(publishedAt);
    }
}
