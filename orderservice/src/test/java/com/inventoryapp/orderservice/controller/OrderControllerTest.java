package com.inventoryapp.orderservice.controller;

import com.inventoryapp.orderservice.dto.CreateOrderRequest;
import com.inventoryapp.orderservice.dto.OrderResponse;
import com.inventoryapp.orderservice.model.Order;
import com.inventoryapp.orderservice.model.OrderStatus;
import com.inventoryapp.orderservice.service.OrderService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerUnitTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void createOrder_success() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(1L);

        OrderResponse response = new OrderResponse(
                1L, "John", "BLR", "john@test.com", "CREATED", BigDecimal.valueOf(500)
        );

        when(orderService.createOrder(request)).thenReturn(response);

        OrderResponse result = orderController.create(request);

        assertEquals(1L, result.getOrderId());
        assertEquals("CREATED", result.getStatus());
    }

    @Test
    void cancelOrder_success() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CANCELLED);

        when(orderService.updateStatus(1L, OrderStatus.CANCELLED, "customer"))
                .thenReturn(order);

        Map<String, String> result = orderController.cancel(1L);

        assertEquals("CANCELLED", result.get("status"));
        assertEquals("Order cancelled successfully", result.get("message"));
    }
}
