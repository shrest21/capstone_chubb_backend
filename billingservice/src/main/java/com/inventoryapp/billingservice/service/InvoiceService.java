package com.inventoryapp.billingservice.service;

import com.inventoryapp.billingservice.dto.InvoiceResponse;
import com.inventoryapp.billingservice.model.Invoice;
import com.inventoryapp.billingservice.model.PaymentStatus;
import com.inventoryapp.billingservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    public InvoiceResponse createInvoice(Long orderId, BigDecimal amount) {
        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setAmount(amount);
        invoice.setStatus(PaymentStatus.UNPAID);
        invoice.setCreatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    public InvoiceResponse getInvoiceByOrderId(Long orderId) {
        Invoice invoice = invoiceRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        return mapToResponse(invoice);
    }
    public void updatePaymentStatus(Long invoiceId, PaymentStatus status) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setStatus(status);
        invoiceRepository.save(invoice);
    }
    private InvoiceResponse mapToResponse(Invoice invoice) {
        return new InvoiceResponse(invoice.getId(), invoice.getOrderId(), invoice.getAmount(),invoice.getStatus());
    }
}
