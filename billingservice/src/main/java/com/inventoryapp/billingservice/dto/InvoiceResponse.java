package com.inventoryapp.billingservice.dto;

import com.inventoryapp.billingservice.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceResponse {
    private Long invoiceId;
    private Long orderId;
    private BigDecimal amount;
    private PaymentStatus status;
}