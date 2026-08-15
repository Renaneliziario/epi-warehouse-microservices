package br.com.renan.almoxarifado.controllers;

import br.com.renan.almoxarifado.exceptions.EmployeeNotFoundException;
import br.com.renan.almoxarifado.exceptions.EpiCategoryNotFoundException;
import br.com.renan.almoxarifado.exceptions.EpiNotFoundException;
import br.com.renan.almoxarifado.exceptions.InsufficientStockException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EpiNotFoundException.class,
            EmployeeNotFoundException.class,
            EpiCategoryNotFoundException.class,
            InsufficientStockException.class
    })
    public ResponseEntity<Map<String, Object>> handleBusinessRule(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", 422,
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("payload inválido");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", 400,
                "message", message
        ));
    }
}