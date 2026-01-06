package com.inventoryapp.orderservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerUnitTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void handleOrderNotFoundException() {

        OrderNotFoundException ex =
                new OrderNotFoundException(1L);

        ResponseEntity<?> response =
                handler.handle(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertTrue(body.containsKey("error"));
    }

    @Test
    void handleLessStockException() {

        LessStockException ex =
                new LessStockException(101L);

        ResponseEntity<?> response =
                handler.handle(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertTrue(body.containsKey("error"));
    }
}
