package com.inventoryapp.warehouseservice.controller;

import com.inventoryapp.warehouseservice.dto.CheckStockRequest;
import com.inventoryapp.warehouseservice.dto.GlobalStockResponse;
import com.inventoryapp.warehouseservice.dto.ReduceStockRequest;
import com.inventoryapp.warehouseservice.dto.StockAvailableResponse;
import com.inventoryapp.warehouseservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    @GetMapping("/stock/{productId}")
    public List<GlobalStockResponse> getGlobalStock(@PathVariable Long productId){
        return inventoryService.getGlobalStock(productId);
    }
    @GetMapping("/stock/check")
    public StockAvailableResponse checkStock(@RequestParam Long productId,@RequestParam Integer quantity){
        return inventoryService.checkAvailability(productId,quantity);
    }
    @PostMapping("/stock/reduce")
    public String deductStock(@RequestBody ReduceStockRequest request) {
        inventoryService.reduceStock(request);
        return "Stock reduced succesfully";
    }
}
