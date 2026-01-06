package com.inventoryapp.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class InvoiceResponse {

    private Long invoiceId;
    private Long orderId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
}