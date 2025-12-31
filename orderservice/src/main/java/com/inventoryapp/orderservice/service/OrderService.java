package com.inventoryapp.orderservice.service;
import com.inventoryapp.orderservice.dto.CreateOrderRequest;
import com.inventoryapp.orderservice.dto.OrderResponse;
import com.inventoryapp.orderservice.exception.OrderNotFoundException;
import com.inventoryapp.orderservice.model.Order;
import com.inventoryapp.orderservice.model.OrderItem;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;

    public OrderResponse createOrder(CreateOrderRequest request) {

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = request.getItems().stream().map(i -> {
            OrderItem item = new OrderItem();
            item.setProductId(i.getProductId());
            item.setQuantity(i.getQuantity());
            item.setPrice(BigDecimal.valueOf(999)); // mock
            item.setOrder(order);
            return item;
        }).toList();

        order.setItems(items);

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        saveHistory(saved, OrderStatus.CREATED, "customer");

        return new OrderResponse(saved.getId(), saved.getStatus().name(), saved.getTotalAmount());
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
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
