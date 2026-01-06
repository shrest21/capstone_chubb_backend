package com.inventoryapp.billingservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.inventoryapp.billingservice.dto.InvoiceResponse;
import com.inventoryapp.billingservice.model.Invoice;
import com.inventoryapp.billingservice.model.PaymentStatus;
import com.inventoryapp.billingservice.repository.InvoiceRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    @Test
    void createInvoice_success() {

        // Arrange
        Long orderId = 101L;
        BigDecimal amount = new BigDecimal("999.99");

        Invoice savedInvoice = new Invoice();
        savedInvoice.setId(1L);
        savedInvoice.setOrderId(orderId);
        savedInvoice.setAmount(amount);
        savedInvoice.setStatus(PaymentStatus.UNPAID);
        savedInvoice.setCreatedAt(LocalDateTime.now());

        when(invoiceRepository.save(any(Invoice.class)))
                .thenAnswer(invocation -> {
                    Invoice inv = invocation.getArgument(0);
                    inv.setId(1L);
                    return inv;
                });


        // Act
        InvoiceResponse response = invoiceService.createInvoice(orderId, amount);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getInvoiceId());
        assertEquals(orderId, response.getOrderId());
        assertEquals(amount, response.getAmount());
        assertEquals(PaymentStatus.UNPAID, response.getStatus());

        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void getAllInvoices_success() {

        // Arrange
        Invoice invoice1 = new Invoice(
                1L, 101L, new BigDecimal("500"),
                PaymentStatus.UNPAID, LocalDateTime.now()
        );

        Invoice invoice2 = new Invoice(
                2L, 102L, new BigDecimal("1000"),
                PaymentStatus.PAID, LocalDateTime.now()
        );

        when(invoiceRepository.findAll())
                .thenReturn(List.of(invoice1, invoice2));

        // Act
        List<InvoiceResponse> responses = invoiceService.getAllInvoices();

        // Assert
        assertEquals(2, responses.size());
        assertEquals(101L, responses.get(0).getOrderId());
        assertEquals(PaymentStatus.PAID, responses.get(1).getStatus());

        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void updatePaymentStatus_success() {

        // Arrange
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setStatus(PaymentStatus.UNPAID);

        when(invoiceRepository.findById(1L))
                .thenReturn(Optional.of(invoice));

        // Act
        invoiceService.updatePaymentStatus(1L, PaymentStatus.PAID);

        // Assert
        assertEquals(PaymentStatus.PAID, invoice.getStatus());
        verify(invoiceRepository).save(invoice);
    }

    @Test
    void updatePaymentStatus_invoiceNotFound_shouldFail() {

        // Arrange
        when(invoiceRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> invoiceService.updatePaymentStatus(99L, PaymentStatus.PAID)
        );

        assertEquals("Invoice not found", ex.getMessage());
        verify(invoiceRepository, never()).save(any());
    }
}