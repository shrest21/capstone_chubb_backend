package com.inventoryapp.warehouseservice.controller;
import com.inventoryapp.warehouseservice.dto.AddWarehouseStockRequest;
import com.inventoryapp.warehouseservice.dto.CreateWarehouseRequest;
import com.inventoryapp.warehouseservice.dto.WarehouseResponse;
import com.inventoryapp.warehouseservice.dto.WarehouseStockResponse;
import com.inventoryapp.warehouseservice.model.Warehouse;
import com.inventoryapp.warehouseservice.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;
    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody CreateWarehouseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.createWarehouse(request));
    }
    @PostMapping("/{code}")
    public void addStock(@PathVariable String code, @RequestBody AddWarehouseStockRequest request) {
        warehouseService.addStock(code, request);
    }
    @GetMapping("/{code}")
    public List<WarehouseStockResponse> getStock(@PathVariable String code) {
        return warehouseService.getWarehouseStock(code);
    }
    @GetMapping
    public List<WarehouseResponse> getAllWarehouses() {
        return warehouseService.getAllWarehouses();
    }
}