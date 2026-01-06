package com.inventoryapp.billingservice.controller;

import com.inventoryapp.billingservice.dto.InvoiceResponse;
import com.inventoryapp.billingservice.dto.UpdatePaymentRequest;
import com.inventoryapp.billingservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    @PostMapping()
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestParam Long orderId, @RequestParam BigDecimal amount) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.createInvoice(orderId, amount));
    }
    @GetMapping()
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePayment(@PathVariable Long id, @RequestBody UpdatePaymentRequest request) {
        invoiceService.updatePaymentStatus(id, request.getStatus());
        return ResponseEntity.ok().build();
    }
}
