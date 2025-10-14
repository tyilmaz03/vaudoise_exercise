package com.example.vaudoise.web;

import com.example.vaudoise.core.exception.BadRequestException;
import com.example.vaudoise.core.exception.ConflictException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.ConstraintViolationException;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    // 400 - Erreurs métier custom
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        return buildErrorResponse(ex.getErrors(), HttpStatus.BAD_REQUEST, "Validation failed");
    }

    // 409 - Conflits
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflict(ConflictException ex) {
        return buildErrorResponse(List.of(ex.getMessage()), HttpStatus.CONFLICT, "Conflict");
    }

    // 409 - Doublons base de données
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "A record with the same unique field already exists";
        return buildErrorResponse(List.of(message), HttpStatus.CONFLICT, "Conflict");
    }

    // 400 - Erreurs de validation sur DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .distinct()
                .toList();

        return buildErrorResponse(messages, HttpStatus.BAD_REQUEST, "Validation failed");
    }

    // 400 - Autres violations 
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> messages = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .distinct()
                .toList();

        return buildErrorResponse(messages, HttpStatus.BAD_REQUEST, "Validation failed");
    }

    // 500 - fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return buildErrorResponse(List.of("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(List<String> messages, HttpStatus status, String errorType) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", errorType);
        body.put("details", messages);
        return ResponseEntity.status(status).body(body);
    }
}
