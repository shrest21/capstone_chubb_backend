package com.inventoryapp.orderservice.feign;

import com.inventoryapp.orderservice.dto.CheckStockRequest;
import com.inventoryapp.orderservice.dto.ReduceStockRequest;
import com.inventoryapp.orderservice.dto.StockAvailableResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="warehouseservice")
public interface WarehouseClient {
    @GetMapping("inventory/stock/check")
    public StockAvailableResponse checkStock(@RequestParam Long productId, @RequestParam Integer quantity);
    @PostMapping("inventory/stock/reduce")
    public String deductStock(@RequestBody ReduceStockRequest request);
}
