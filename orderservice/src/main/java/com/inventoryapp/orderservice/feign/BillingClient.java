package com.inventoryapp.orderservice.feign;

import com.inventoryapp.orderservice.dto.InvoiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "billingservice")
public interface BillingClient {
    @PostMapping("/invoices") InvoiceResponse createInvoice(@RequestParam("orderId") Long orderId, @RequestParam("amount") BigDecimal amount);
}