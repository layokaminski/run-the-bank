package com.banco.santander.exceptions;

import com.banco.santander.exceptions.customs.AccessDeniedException;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.exceptions.customs.IllegalStateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(final MethodArgumentNotValidException ex) {
        final var errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        final var errorDTO = ErrorResponse.builder()
                .message(MessageError.VALIDATION_ERROR.name())
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundErrors(final EntityNotFoundException ex) {
        final var errorMessage = ex.getMessage();
        final var errorDTO = ErrorResponse.builder()
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateErrors(final IllegalStateException ex) {
        final var errorMessage = ex.getMessage();
        final var errorDTO = ErrorResponse.builder()
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedErrors(final AccessDeniedException ex) {
        final var errorMessage = ex.getMessage();
        final var errorDTO = ErrorResponse.builder()
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentErrors(final IllegalArgumentException ex) {
        final var errorMessage = ex.getMessage();
        final var errorDTO = ErrorResponse.builder()
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
