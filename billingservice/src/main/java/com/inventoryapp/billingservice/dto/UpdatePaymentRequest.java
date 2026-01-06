package com.inventoryapp.billingservice.dto;
import com.inventoryapp.billingservice.model.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentRequest {
    private PaymentStatus status;
}
