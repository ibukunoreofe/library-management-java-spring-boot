package com.eaproc.tutorials.librarymanagement.util;

import com.eaproc.tutorials.librarymanagement.web.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class ValidationUtil {

    public static ResponseEntity<?> handleValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Validation errors: " + errors));
        }
        return null;
    }
}
