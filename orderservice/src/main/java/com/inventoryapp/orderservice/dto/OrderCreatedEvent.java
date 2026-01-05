package com.inventoryapp.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderCreatedEvent {

    private Long orderId;
    private String userEmail;
    private BigDecimal totalAmount;
}
