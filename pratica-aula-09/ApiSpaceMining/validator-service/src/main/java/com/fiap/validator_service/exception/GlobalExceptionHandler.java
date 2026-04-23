package com.fiap.validator_service.exception;

import com.fiap.validator_service.dto.ValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCommandException.class)
    public ResponseEntity<ValidationResponse> handleInvalidCommand(InvalidCommandException ex) {
        ValidationResponse response = new ValidationResponse(
                "ERROR",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SimulatedCommunicationException.class)
    public ResponseEntity<ValidationResponse> handleSimulatedFailure(SimulatedCommunicationException ex) {
        ValidationResponse response = new ValidationResponse(
                "ERROR",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ValidationResponse> handleGeneric(Exception ex) {
        ValidationResponse response = new ValidationResponse(
                "ERROR",
                "Erro interno no validator-service.",
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}