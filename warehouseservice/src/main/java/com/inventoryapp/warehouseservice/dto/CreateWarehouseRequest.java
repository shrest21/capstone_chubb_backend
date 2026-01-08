package com.inventoryapp.warehouseservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWarehouseRequest {
    @NotBlank(message = "Warehouse code is required")
    private String code;
    @NotBlank(message = "Warehouse location is required")
    private String location;
}
