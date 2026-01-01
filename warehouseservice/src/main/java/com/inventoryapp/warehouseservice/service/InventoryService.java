package com.inventoryapp.warehouseservice.service;
import com.inventoryapp.warehouseservice.dto.GlobalStockResponse;
import com.inventoryapp.warehouseservice.repository.WarehouseStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final WarehouseStockRepository stockRepository;
    public List<GlobalStockResponse> getGlobalStock(Long productId) {
        return stockRepository.findAll().stream().filter(s -> s.getProductId().equals(productId))
                .map(s -> new GlobalStockResponse(s.getWarehouse().getCode(), s.getQuantity())).toList();
    }
}