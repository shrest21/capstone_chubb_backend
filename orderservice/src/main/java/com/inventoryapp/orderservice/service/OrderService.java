package com.inventoryapp.orderservice.service;
import com.inventoryapp.orderservice.dto.*;
import com.inventoryapp.orderservice.exception.LessStockException;
import com.inventoryapp.orderservice.exception.OrderNotFoundException;
import com.inventoryapp.orderservice.feign.WarehouseClient;
import com.inventoryapp.orderservice.feign.ProductClient;
import com.inventoryapp.orderservice.feign.ProductResponse;
import com.inventoryapp.orderservice.model.Order;
import com.inventoryapp.orderservice.model.OrderStatus;
import com.inventoryapp.orderservice.model.OrderStatusHistory;
import com.inventoryapp.orderservice.repository.OrderRepository;
import com.inventoryapp.orderservice.repository.OrderStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final ProductClient productClient;
    private final WarehouseClient warehouseClient;

    public OrderResponse createOrder(CreateOrderRequest request) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(OrderItemRequest item:request.getItems())
        {
            StockAvailableResponse check = warehouseClient.checkStock(item.getProductId(),item.getQuantity());
            if(!check.isAvailable()){
                throw new LessStockException(item.getProductId());
            }
        }
        for (OrderItemRequest item : request.getItems()) {
            ProductResponse product = productClient.getProductById(item.getProductId());
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setCustomerName(request.getCustomerName());
        order.setAddress(request.getAddress());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);
        for(OrderItemRequest item:request.getItems())
        {
            ReduceStockRequest reduceRequest = new ReduceStockRequest();
            reduceRequest.setProductId(item.getProductId());
            reduceRequest.setQuantity(item.getQuantity());
            reduceRequest.setOrderId(saved.getId());
            warehouseClient.deductStock(reduceRequest);
        }
        return new OrderResponse(saved.getId(), saved.getCustomerName(), saved.getAddress(), saved.getStatus().name(), saved.getTotalAmount());
    }
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }
    public Page<Order> getOrdersByUser(Long userId, Pageable pageable) {
        return orderRepository.findByCustomerId(userId, pageable);
    }
    public Order updateStatus(Long id, OrderStatus newStatus, String role) {
        Order order = getOrder(id);
        OrderStatus old = order.getStatus();
        order.setStatus(newStatus);
        saveHistory(order, newStatus, role);
        return orderRepository.save(order);
    }
    public void saveHistory(Order order, OrderStatus status, String by) {
        OrderStatusHistory h = new OrderStatusHistory();
        h.setOrder(order);
        h.setStatus(status);
        h.setChangedBy(by);
        h.setChangedAt(LocalDateTime.now());
        historyRepository.save(h);
    }
}