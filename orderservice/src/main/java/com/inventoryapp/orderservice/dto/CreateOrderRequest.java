package com.inventoryapp.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private Long customerId;
    private String customerName;
    private String email;
    private String address;
    private List<OrderItemRequest> items;
}