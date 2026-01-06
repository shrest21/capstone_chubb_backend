package com.inventoryapp.orderservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.inventoryapp.orderservice.config.RabbitMQConstants;
import com.inventoryapp.orderservice.dto.*;
import com.inventoryapp.orderservice.exception.LessStockException;
import com.inventoryapp.orderservice.exception.OrderNotFoundException;
import com.inventoryapp.orderservice.feign.BillingClient;
import com.inventoryapp.orderservice.feign.ProductClient;
import com.inventoryapp.orderservice.feign.ProductResponse;
import com.inventoryapp.orderservice.feign.WarehouseClient;
import com.inventoryapp.orderservice.model.Order;
import com.inventoryapp.orderservice.model.OrderStatus;
import com.inventoryapp.orderservice.repository.OrderRepository;
import com.inventoryapp.orderservice.repository.OrderStatusHistoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderStatusHistoryRepository historyRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private WarehouseClient warehouseClient;

    @Mock
    private BillingClient billingClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderService orderService;

    private CreateOrderRequest request;

    @BeforeEach
    void setup() {
        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(101L);
        item.setQuantity(2);

        request = new CreateOrderRequest();
        request.setCustomerId(1L);
        request.setCustomerName("John");
        request.setAddress("Bangalore");
        request.setEmail("john@test.com");
        request.setItems(List.of(item));
    }

    @Test
    void createOrder_success() {

        StockAvailableResponse stockResponse = new StockAvailableResponse();
        stockResponse.setAvailable(true);

        when(warehouseClient.checkStock(101L, 2))
                .thenReturn(stockResponse);

        ProductResponse product = new ProductResponse();
        product.setId(101L);
        product.setName("Mouse");
        product.setBrand("Logitech");
        product.setPrice(BigDecimal.valueOf(500));

        when(productClient.getProductById(101L))
                .thenReturn(product);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> {
                    Order o = inv.getArgument(0);
                    o.setId(1L);
                    return o;
                });

        OrderResponse response = orderService.createOrder(request);

        assertEquals(1L, response.getOrderId());
        assertEquals("CREATED", response.getStatus());
        assertEquals(BigDecimal.valueOf(1000), response.getTotalAmount());

        verify(billingClient).createInvoice(1L, BigDecimal.valueOf(1000));
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConstants.ORDER_EXCHANGE),
                eq(RabbitMQConstants.ORDER_CREATED_ROUTING_KEY),
                any(OrderCreatedEvent.class)
        );
        verify(warehouseClient).deductStock(any(ReduceStockRequest.class));
    }

    @Test
    void createOrder_lessStock_shouldFail() {

        StockAvailableResponse stockResponse = new StockAvailableResponse();
        stockResponse.setAvailable(false);

        when(warehouseClient.checkStock(101L, 2))
                .thenReturn(stockResponse);

        assertThrows(
                LessStockException.class,
                () -> orderService.createOrder(request)
        );

        verify(orderRepository, never()).save(any());
        verify(billingClient, never()).createInvoice(any(), any());
    }

    @Test
    void getOrder_success() {

        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        Order result = orderService.getOrder(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getOrder_notFound() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrder(1L)
        );
    }

    @Test
    void getOrdersByUser_success() {

        Order order = new Order();
        order.setCustomerId(1L);

        Page<Order> page =
                new PageImpl<>(List.of(order));

        when(orderRepository.findByCustomerId(eq(1L), any()))
                .thenReturn(page);

        Page<Order> result =
                orderService.getOrdersByUser(1L, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateStatus_success() {

        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        Order updated =
                orderService.updateStatus(1L, OrderStatus.APPROVED, "ADMIN");

        assertEquals(OrderStatus.APPROVED, updated.getStatus());
        verify(historyRepository).save(any());
    }
}
