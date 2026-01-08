package com.inventoryapp.billingservice.dto;
import com.inventoryapp.billingservice.model.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentRequest {
    @NotNull(message = "Payment status is required")
    private PaymentStatus status;
}
