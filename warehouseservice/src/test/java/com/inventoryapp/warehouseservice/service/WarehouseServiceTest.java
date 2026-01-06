package com.inventoryapp.warehouseservice.service;

import com.inventoryapp.warehouseservice.dto.*;
import com.inventoryapp.warehouseservice.feign.ProductClient;
import com.inventoryapp.warehouseservice.feign.ProductFeignResponse;
import com.inventoryapp.warehouseservice.model.*;
import com.inventoryapp.warehouseservice.repository.WarehouseRepository;
import com.inventoryapp.warehouseservice.repository.WarehouseStockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private WarehouseStockRepository stockRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private WarehouseService warehouseService;

    @Test
    void createWarehouse_success() {
        CreateWarehouseRequest request =
                new CreateWarehouseRequest("WH1", "Bangalore");

        Warehouse saved = new Warehouse(1L, "WH1", "Bangalore");

        when(warehouseRepository.save(any())).thenReturn(saved);

        Warehouse result = warehouseService.createWarehouse(request);

        assertEquals("WH1", result.getCode());
        assertEquals("Bangalore", result.getLocation());
    }

    @Test
    void addStock_newProduct_success() {
        Warehouse wh = new Warehouse(1L, "WH1", "BLR");

        when(warehouseRepository.findByCode("WH1"))
                .thenReturn(Optional.of(wh));

        when(stockRepository.findByWarehouseIdAndProductId(1L, 101L))
                .thenReturn(Optional.empty());

        AddWarehouseStockRequest request =
                new AddWarehouseStockRequest(101L, 10);

        warehouseService.addStock("WH1", request);

        verify(stockRepository).save(any(WarehouseStock.class));
    }

    @Test
    void addStock_warehouseNotFound_shouldFail() {
        when(warehouseRepository.findByCode("WH1"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> warehouseService.addStock("WH1",
                        new AddWarehouseStockRequest(101L, 5))
        );

        assertTrue(ex.getMessage().contains("Warehouse not found"));
    }

    @Test
    void getWarehouseStock_success() {
        Warehouse wh = new Warehouse(1L, "WH1", "BLR");
        WarehouseStock stock = new WarehouseStock(1L, wh, 101L, 20);

        when(warehouseRepository.findByCode("WH1"))
                .thenReturn(Optional.of(wh));

        when(stockRepository.findByWarehouseId(1L))
                .thenReturn(List.of(stock));

        ProductFeignResponse product =
                new ProductFeignResponse();
        product.setId(101L);
        product.setName("Mouse");

        when(productClient.getProductById(101L)).thenReturn(product);

        List<WarehouseStockResponse> result =
                warehouseService.getWarehouseStock("WH1");

        assertEquals(1, result.size());
        assertEquals("Mouse", result.get(0).getProductName());
        assertEquals(20, result.get(0).getQuantity());
    }

    @Test
    void getAllWarehouses_success() {
        when(warehouseRepository.findAll())
                .thenReturn(List.of(
                        new Warehouse(1L, "WH1", "BLR")
                ));

        List<WarehouseResponse> result =
                warehouseService.getAllWarehouses();

        assertEquals(1, result.size());
        assertEquals("WH1", result.get(0).getCode());
    }
}