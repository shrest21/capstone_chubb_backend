package com.inventoryapp.warehouseservice.service;
import com.inventoryapp.warehouseservice.dto.AddWarehouseStockRequest;
import com.inventoryapp.warehouseservice.dto.CreateWarehouseRequest;
import com.inventoryapp.warehouseservice.dto.WarehouseResponse;
import com.inventoryapp.warehouseservice.dto.WarehouseStockResponse;
import com.inventoryapp.warehouseservice.feign.ProductClient;
import com.inventoryapp.warehouseservice.model.Warehouse;
import com.inventoryapp.warehouseservice.model.WarehouseStock;
import com.inventoryapp.warehouseservice.repository.WarehouseRepository;
import com.inventoryapp.warehouseservice.repository.WarehouseStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseStockRepository stockRepository;
    private final ProductClient productClient;

    public Warehouse createWarehouse(CreateWarehouseRequest request) {
        Warehouse warehouse = new Warehouse();
        warehouse.setCode(request.getCode());
        warehouse.setLocation(request.getLocation());
        return warehouseRepository.save(warehouse);
    }
    public void addStock(String warehouseCode, AddWarehouseStockRequest request) {
        Warehouse warehouse = warehouseRepository.findByCode(warehouseCode)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        WarehouseStock stock = stockRepository
                .findByWarehouseIdAndProductId(warehouse.getId(), request.getProductId())
                .orElse(new WarehouseStock(null, warehouse, request.getProductId(), 0));
        stock.setQuantity(stock.getQuantity() + request.getQuantity());
        stockRepository.save(stock);
    }
    public List<WarehouseStockResponse> getWarehouseStock(String warehouseCode) {
        Warehouse warehouse = warehouseRepository.findByCode(warehouseCode).orElseThrow(() -> new RuntimeException("Warehouse not found"));
        return stockRepository.findByWarehouseId(warehouse.getId())
                .stream()
                .map(stock -> {
                    String productName = productClient.getProductById(stock.getProductId()).getName();
                    return new WarehouseStockResponse(stock.getProductId(), productName, stock.getQuantity());
                })
                .toList();
    }
    public List<WarehouseResponse> getAllWarehouses() {
        return warehouseRepository.findAll()
                .stream()
                .map(w -> new WarehouseResponse(w.getCode(), w.getLocation()))
                .toList();
    }
}
