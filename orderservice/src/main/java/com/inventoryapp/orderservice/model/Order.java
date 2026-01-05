package com.inventoryapp.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String customerName;
    private String address;
    private String email;
    private BigDecimal totalAmount;

    private String warehouseCode;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatusHistory> statusHistory;
}