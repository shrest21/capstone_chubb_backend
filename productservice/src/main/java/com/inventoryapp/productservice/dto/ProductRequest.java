package com.inventoryapp.productservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
}
