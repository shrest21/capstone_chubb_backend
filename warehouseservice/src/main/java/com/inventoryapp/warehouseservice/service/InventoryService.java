package com.inventoryapp.warehouseservice.service;
import com.inventoryapp.warehouseservice.dto.CheckStockRequest;
import com.inventoryapp.warehouseservice.dto.GlobalStockResponse;
import com.inventoryapp.warehouseservice.dto.ReduceStockRequest;
import com.inventoryapp.warehouseservice.dto.StockAvailableResponse;
import com.inventoryapp.warehouseservice.model.StockMovement;
import com.inventoryapp.warehouseservice.model.Warehouse;
import com.inventoryapp.warehouseservice.model.WarehouseStock;
import com.inventoryapp.warehouseservice.repository.StockMovementRepository;
import com.inventoryapp.warehouseservice.repository.WarehouseStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final WarehouseStockRepository stockRepository;
    private final StockMovementRepository movementRepository;
    public List<GlobalStockResponse> getGlobalStock(Long productId) {
        return stockRepository.findAll().stream().filter(s -> s.getProductId().equals(productId))
                .map(s -> new GlobalStockResponse(s.getWarehouse().getCode(), s.getQuantity())).toList();
    }
    public StockAvailableResponse checkAvailability(Long productId, Integer quantity) {
        return stockRepository.findAll().stream()
                .filter(s -> s.getProductId().equals(productId))
                .filter(s -> s.getQuantity() >= quantity).findFirst()
                .map(s -> new StockAvailableResponse(true, s.getWarehouse().getCode()))
                .orElse(new StockAvailableResponse(false, null));
    }
    public void reduceStock(ReduceStockRequest request) {

        WarehouseStock stock = stockRepository.findAll().stream()
                .filter(s -> s.getProductId().equals(request.getProductId()))
                .filter(s -> s.getQuantity() >= request.getQuantity())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Insufficient stock"));
        stock.setQuantity(stock.getQuantity() - request.getQuantity());
        stockRepository.save(stock);
        recordMovement(stock.getWarehouse(), request.getProductId(), request.getQuantity(), request.getOrderId());
    }
    private void recordMovement(Warehouse warehouse, Long productId, Integer qtyChange, Long orderId) {
        StockMovement movement = new StockMovement();
        movement.setWarehouse(warehouse);
        movement.setProductId(productId);
        movement.setQuantityChange(qtyChange);
        movement.setOrderId(orderId);
        movementRepository.save(movement);
    }
}