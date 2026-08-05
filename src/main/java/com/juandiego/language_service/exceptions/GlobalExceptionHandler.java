package com.juandiego.language_service.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.juandiego.language_service.responses.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(LanguageNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleLanguageNotFound(
        LanguageNotFoundException ex, HttpServletRequest  request
    ){
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.NOT_FOUND.value(), ex.getMessage(), Map.of("path", request.getRequestURI())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}