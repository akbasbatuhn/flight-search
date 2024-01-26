package com.amadeus.flightsearch.exception;

import com.amadeus.flightsearch.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest request) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .errorCode(HttpStatus.NOT_FOUND)
                .errorMessage(exception.getMessage())
                .apiPath(request.getDescription(false))
                .errorTimestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceAlreadyExistException(
            ResourceAlreadyExistException exception, WebRequest request) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMessage(exception.getMessage())
                .apiPath(request.getDescription(false))
                .errorTimestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
