package com.inventoryapp.billingservice.repository;

import com.inventoryapp.billingservice.model.Invoice;
import com.inventoryapp.billingservice.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrderId(Long orderId);
    List<Invoice> findByStatus(PaymentStatus status);
}
