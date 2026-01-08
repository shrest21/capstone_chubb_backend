package com.inventoryapp.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.inventoryapp.orderservice.model.OrderStatus;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {
    @NotNull(message = "Order status is required")
    private OrderStatus status;
    @NotNull(message = "Role of who updated should be provided")
    private String role;
}
