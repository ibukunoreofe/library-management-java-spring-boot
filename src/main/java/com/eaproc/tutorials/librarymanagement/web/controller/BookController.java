package com.eaproc.tutorials.librarymanagement.web.controller;

import com.eaproc.tutorials.librarymanagement.config.annotation.AdminOnlyEndpoint;
import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.service.BookService;
import com.eaproc.tutorials.librarymanagement.web.dto.BookDto;
import com.eaproc.tutorials.librarymanagement.web.request.book.BookRequest;
import com.eaproc.tutorials.librarymanagement.web.response.ErrorResponse;
import com.eaproc.tutorials.librarymanagement.web.response.PaginatedResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    private final ModelMapper modelMapper;

    public BookController(BookService bookService) {
        this.bookService = bookService;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping
    @AdminOnlyEndpoint
    public ResponseEntity<?> getAllBooks(@RequestParam(value = "paginate", required = false, defaultValue = "false") boolean paginate,
                                         @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                         @RequestParam(value = "search", required = false) String search) {
        if (paginate) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
            Page<BookEntity> bookPage;
            if (search != null && !search.isEmpty()) {
                bookPage = bookService.searchBooks(search, pageable);
            } else {
                bookPage = bookService.getAllBooks(pageable);
            }
            List<BookDto> books = bookPage.getContent().stream()
                    .map(book -> modelMapper.map(book, BookDto.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new PaginatedResponse<>(books, bookPage.getTotalPages(), bookPage.getTotalElements()));
        } else {
            List<BookDto> books;
            if (search != null && !search.isEmpty()) {
                books = bookService.searchBooks(search).stream()
                        .map(book -> modelMapper.map(book, BookDto.class))
                        .collect(Collectors.toList());
            } else {
                books = bookService.getAllBooks().stream()
                        .map(book -> modelMapper.map(book, BookDto.class))
                        .collect(Collectors.toList());
            }
            return ResponseEntity.ok(books);
        }
    }

    @GetMapping("/{id}")
    @AdminOnlyEndpoint
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Optional<BookEntity> bookEntity = bookService.getBookById(id);
        if (bookEntity.isPresent()) {
            BookDto bookDto = modelMapper.map(bookEntity.get(), BookDto.class);
            return ResponseEntity.ok(bookDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Book not found"));
        }
    }

    @PostMapping
    @AdminOnlyEndpoint
    public ResponseEntity<?> createBook(@Valid @RequestBody BookRequest createBookRequest) {
        BookEntity bookEntity = modelMapper.map(createBookRequest, BookEntity.class);

        BookEntity createdBook = bookService.createBook(bookEntity);
        BookDto createdBookDto = modelMapper.map(createdBook, BookDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookDto);
    }

    @PutMapping("/{id}")
    @AdminOnlyEndpoint
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest updateBookRequest) {
        try {
            BookEntity bookEntity = modelMapper.map(updateBookRequest, BookEntity.class);
            Optional<BookEntity> updatedBook = bookService.updateBook(id, bookEntity);
            if (updatedBook.isPresent()) {
                BookDto updatedBookDto = modelMapper.map(updatedBook.get(), BookDto.class);
                return ResponseEntity.ok(updatedBookDto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Book not found"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @AdminOnlyEndpoint
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (bookService.getBookById(id).isPresent()) {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Book not found"));
        }
    }
}
