package com.inventoryapp.orderservice.controller;

import com.inventoryapp.orderservice.dto.*;
import com.inventoryapp.orderservice.model.Order;
import com.inventoryapp.orderservice.model.OrderStatus;
import com.inventoryapp.orderservice.model.OrderStatusHistory;
import com.inventoryapp.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }
    @GetMapping("/{orderId}")
    public Order get(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }
    @GetMapping("/user/{userId}")
    public Page<OrderSummaryResponse> getByUser(@PathVariable Long userId, Pageable pageable) {
        return orderService.getOrdersByUser(userId, pageable)
                .map(o -> new OrderSummaryResponse(o.getId(), o.getStatus().name(), o.getTotalAmount(), o.getCreatedAt()));
    }
    @GetMapping("/{id}/status")
    public List<OrderStatusHistory> statusHistory(@PathVariable Long id) {
        return orderService.getOrder(id).getStatusHistory();
    }
    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest req) {
        return orderService.updateStatus(id, req.getStatus(), "admin");
    }
    @PutMapping("/cancel/{id}")
    public Map<String, String> cancel(@PathVariable Long id) {
        Order o = orderService.updateStatus(id, OrderStatus.CANCELLED, "customer");
        return Map.of("orderId", o.getId().toString(), "status", o.getStatus().name(), "message", "Order cancelled successfully");
    }
    @GetMapping
    public List<OrderStatusResponse> getAllOrders() {
        return orderService.getAllOrderStatus();
    }
}