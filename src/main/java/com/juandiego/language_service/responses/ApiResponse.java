package com.juandiego.language_service.responses;

public record ApiResponse<T>(
        int statusCode,
        String message,
        T data
) {
}