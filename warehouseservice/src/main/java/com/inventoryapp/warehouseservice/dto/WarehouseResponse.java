package com.inventoryapp.warehouseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WarehouseResponse {
    private String code;
    private String location;
}