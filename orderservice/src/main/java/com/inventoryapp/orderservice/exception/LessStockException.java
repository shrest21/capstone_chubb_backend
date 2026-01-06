package com.inventoryapp.orderservice.exception;

public class LessStockException extends RuntimeException{
    public LessStockException(Long productId)
    {
        super("Stock not available for your order quantity, product id : "+productId);
    }
}
