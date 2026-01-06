package com.inventoryapp.warehouseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReduceStockRequest {
    private Long productId;
    private Integer quantity;
    private Long orderId;
}
