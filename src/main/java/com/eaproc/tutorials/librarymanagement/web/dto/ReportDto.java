package com.eaproc.tutorials.librarymanagement.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private int copies;
    private int checkedOutCount;
    private int quantityLeft;
    private int timesCheckedOutAndReturned; // New field
}
