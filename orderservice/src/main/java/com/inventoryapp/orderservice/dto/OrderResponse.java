package com.inventoryapp.orderservice.dto;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String status;
    private BigDecimal totalAmount;
}