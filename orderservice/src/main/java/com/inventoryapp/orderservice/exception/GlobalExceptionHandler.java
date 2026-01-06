package com.inventoryapp.orderservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handle(OrderNotFoundException error) {
        return ResponseEntity.status(404).body(Map.of("error", error.getMessage()));
    }
    @ExceptionHandler(LessStockException.class)
    public ResponseEntity<?> handle(LessStockException error) {
        return ResponseEntity.status(404).body(Map.of("error", error.getMessage()));
    }
}

