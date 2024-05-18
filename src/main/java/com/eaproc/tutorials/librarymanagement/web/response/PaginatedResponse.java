package com.eaproc.tutorials.librarymanagement.web.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PaginatedResponse<T> {
    // Getters and setters
    private List<T> content;
    private int totalPages;
    private long totalElements;

    public PaginatedResponse(List<T> content, int totalPages, long totalElements) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

}
