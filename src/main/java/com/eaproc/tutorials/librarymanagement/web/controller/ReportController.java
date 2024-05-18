package com.eaproc.tutorials.librarymanagement.web.controller;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.service.BookService;
import com.eaproc.tutorials.librarymanagement.service.CheckoutService;
import com.eaproc.tutorials.librarymanagement.web.dto.ReportDto;
import com.eaproc.tutorials.librarymanagement.web.response.PaginatedResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final BookService bookService;
    private final CheckoutService checkoutService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReportController(BookService bookService, CheckoutService checkoutService) {
        this.bookService = bookService;
        this.checkoutService = checkoutService;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping("/books")
    public ResponseEntity<?> getAllBooks(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                         @RequestParam(value = "search", required = false) String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<BookEntity> bookPage;

        if (search != null && !search.isEmpty()) {
            bookPage = bookService.searchBooks(search, pageable);
        } else {
            bookPage = bookService.getAllBooks(pageable);
        }

        List<ReportDto> books = bookPage.getContent().stream().map(book -> {
            ReportDto reportDto = modelMapper.map(book, ReportDto.class);
            int checkedOutCount = checkoutService.countCheckedOutBooks(book.getId());
            reportDto.setCheckedOutCount(checkedOutCount);
            reportDto.setQuantityLeft(book.getCopies() - checkedOutCount);
            return reportDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(new PaginatedResponse<>(books, bookPage.getTotalPages(), bookPage.getTotalElements()));
    }
}
