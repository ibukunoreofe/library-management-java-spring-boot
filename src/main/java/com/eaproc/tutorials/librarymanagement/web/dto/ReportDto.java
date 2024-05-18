package com.eaproc.tutorials.librarymanagement.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private Long id;

    private String title;

    private String author;

    private String isbn;

    private Date publishedAt;

    private Integer copies;

    private Integer checkedOutCount;

    private Integer quantityLeft;
}
