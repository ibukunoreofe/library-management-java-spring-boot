package com.eaproc.tutorials.librarymanagement.web.controller;

import com.eaproc.tutorials.librarymanagement.config.providers.CustomUserDetails;
import com.eaproc.tutorials.librarymanagement.domain.model.CheckoutEntity;
import com.eaproc.tutorials.librarymanagement.service.CheckoutService;
import com.eaproc.tutorials.librarymanagement.web.dto.CheckoutDto;
import com.eaproc.tutorials.librarymanagement.web.request.checkout.CheckoutRequest;
import com.eaproc.tutorials.librarymanagement.web.response.ErrorResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkouts")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final ModelMapper modelMapper;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
        this.modelMapper = new ModelMapper();
    }

    @PostMapping
    public ResponseEntity<?> checkoutBook(@Valid @RequestBody CheckoutRequest checkoutRequest, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            CheckoutEntity checkoutEntity = checkoutService.checkoutBook(checkoutRequest.getBookId(), user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(checkoutEntity, CheckoutDto.class));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }
}
