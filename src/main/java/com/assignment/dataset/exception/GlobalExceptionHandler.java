package com.assignment.dataset.exception;

import com.assignment.dataset.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidQueryParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidQueryParam(InvalidQueryParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .error("Bad Request")
                        .message(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(System.currentTimeMillis())
                        .build()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .error("Not Found")
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND.value())
                        .timestamp(System.currentTimeMillis())
                        .build()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .error("Malformed JSON")
                        .message("Request body contains invalid or unparseable JSON payload.")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(System.currentTimeMillis())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .error("Internal Server Error")
                        .message(ex.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .timestamp(System.currentTimeMillis())
                        .build()
        );
    }
}