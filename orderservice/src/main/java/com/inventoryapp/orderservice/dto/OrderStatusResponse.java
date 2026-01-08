package com.inventoryapp.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderStatusResponse {
    private Long orderId;
    private String status;
}