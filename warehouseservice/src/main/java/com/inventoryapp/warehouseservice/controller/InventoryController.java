package com.inventoryapp.warehouseservice.controller;

import com.inventoryapp.warehouseservice.dto.GlobalStockResponse;
import com.inventoryapp.warehouseservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    @GetMapping("/stock/product/{productId}")
    public List<GlobalStockResponse> getGlobalStock(@PathVariable Long productId){
        return inventoryService.getGlobalStock(productId);
    }
}
