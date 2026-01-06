package com.inventoryapp.billingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryapp.billingservice.dto.InvoiceResponse;
import com.inventoryapp.billingservice.dto.UpdatePaymentRequest;
import com.inventoryapp.billingservice.model.PaymentStatus;
import com.inventoryapp.billingservice.service.InvoiceService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createInvoice_success() throws Exception {

        InvoiceResponse response = new InvoiceResponse(
                1L,
                101L,
                new BigDecimal("500.00"),
                PaymentStatus.UNPAID,
                LocalDateTime.now()
        );

        when(invoiceService.createInvoice(101L, new BigDecimal("500.00")))
                .thenReturn(response);

        mockMvc.perform(post("/invoices")
                        .param("orderId", "101")
                        .param("amount", "500.00"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invoiceId").value(1))
                .andExpect(jsonPath("$.orderId").value(101))
                .andExpect(jsonPath("$.status").value("UNPAID"));

        verify(invoiceService).createInvoice(101L, new BigDecimal("500.00"));
    }

    @Test
    void getAllInvoices_success() throws Exception {

        List<InvoiceResponse> invoices = List.of(
                new InvoiceResponse(
                        1L,
                        101L,
                        new BigDecimal("500"),
                        PaymentStatus.UNPAID,
                        LocalDateTime.now()
                ),
                new InvoiceResponse(
                        2L,
                        102L,
                        new BigDecimal("1000"),
                        PaymentStatus.PAID,
                        LocalDateTime.now()
                )
        );

        when(invoiceService.getAllInvoices()).thenReturn(invoices);

        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].orderId").value(101))
                .andExpect(jsonPath("$[1].status").value("PAID"));

        verify(invoiceService).getAllInvoices();
    }

    @Test
    void updatePayment_success() throws Exception {

        UpdatePaymentRequest request = new UpdatePaymentRequest();
        request.setStatus(PaymentStatus.PAID);

        mockMvc.perform(put("/invoices/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(invoiceService).updatePaymentStatus(1L, PaymentStatus.PAID);
    }
}
