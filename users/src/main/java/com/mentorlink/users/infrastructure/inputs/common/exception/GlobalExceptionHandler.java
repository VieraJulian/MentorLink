package com.mentorlink.users.infrastructure.inputs.common.exception;

import com.mentorlink.users.infrastructure.inputs.common.response.ApiResponse;
import com.mentorlink.users.infrastructure.outputs.auth.exception.KeycloakUserCreationException;
import com.mentorlink.users.infrastructure.outputs.auth.exception.TokenRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationError(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ApiResponse<List<String>> response = ApiResponse.<List<String>>builder()
                .status("error")
                .message("Validation Error" )
                .data(errors)
                .metadata(null)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnexpectedError(Exception ex) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status("error")
                .message("An unexpected error occurred")
                .data(ex.getMessage())
                .metadata(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(KeycloakUserCreationException.class)
    public ResponseEntity<ApiResponse<?>> handleKeycloakUserCreationError(KeycloakUserCreationException ex) {
        log.error("Keycloak User Creation Error: {}", ex.getMessage());

        ApiResponse<String> response = ApiResponse.<String>builder()
                .status("error")
                .message("Keycloak User Creation Error")
                .data(ex.getMessage())
                .metadata(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(TokenRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleTokenRequestError(TokenRequestException ex) {
        log.error("Token Request Error: {}", ex.getMessage());

        ApiResponse<String> response = ApiResponse.<String>builder()
                .status("error")
                .message("Token Request Error")
                .data(ex.getMessage())
                .metadata(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorizedError(HttpClientErrorException.Unauthorized ex) {
        log.error("Unauthorized Error: {}", ex.getMessage());

        ApiResponse<String> response = ApiResponse.<String>builder()
                .status("error")
                .message("Unauthorized Error")
                .data(ex.getMessage())
                .metadata(null)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
