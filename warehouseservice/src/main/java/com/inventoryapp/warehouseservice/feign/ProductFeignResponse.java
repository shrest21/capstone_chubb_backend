package com.inventoryapp.warehouseservice.feign;
import lombok.*;
import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFeignResponse {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
}
