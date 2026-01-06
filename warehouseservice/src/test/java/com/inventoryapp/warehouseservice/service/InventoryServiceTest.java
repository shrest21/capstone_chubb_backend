package com.inventoryapp.warehouseservice.service;

import com.inventoryapp.warehouseservice.dto.*;
import com.inventoryapp.warehouseservice.model.*;
import com.inventoryapp.warehouseservice.repository.StockMovementRepository;
import com.inventoryapp.warehouseservice.repository.WarehouseStockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private WarehouseStockRepository stockRepository;

    @Mock
    private StockMovementRepository movementRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void getGlobalStock_success() {
        Warehouse wh = new Warehouse(1L, "WH1", "BLR");
        WarehouseStock stock = new WarehouseStock(1L, wh, 101L, 10);

        when(stockRepository.findAll()).thenReturn(List.of(stock));

        List<GlobalStockResponse> result =
                inventoryService.getGlobalStock(101L);

        assertEquals(1, result.size());
        assertEquals("WH1", result.get(0).getWarehouseCode());
        assertEquals(10, result.get(0).getQuantity());
    }

    @Test
    void checkAvailability_available() {
        Warehouse wh = new Warehouse(1L, "WH1", "BLR");
        WarehouseStock stock = new WarehouseStock(1L, wh, 101L, 20);

        when(stockRepository.findAll()).thenReturn(List.of(stock));

        StockAvailableResponse response =
                inventoryService.checkAvailability(101L, 5);

        assertTrue(response.isAvailable());
        assertEquals("WH1", response.getWarehouseCode());
    }

    @Test
    void checkAvailability_notAvailable() {
        when(stockRepository.findAll()).thenReturn(List.of());

        StockAvailableResponse response =
                inventoryService.checkAvailability(101L, 5);

        assertFalse(response.isAvailable());
        assertNull(response.getWarehouseCode());
    }

    @Test
    void reduceStock_success() {
        Warehouse wh = new Warehouse(1L, "WH1", "BLR");
        WarehouseStock stock = new WarehouseStock(1L, wh, 101L, 20);

        when(stockRepository.findAll()).thenReturn(List.of(stock));

        ReduceStockRequest request =
                new ReduceStockRequest(101L, 5, 99L);

        inventoryService.reduceStock(request);

        assertEquals(15, stock.getQuantity());
        verify(stockRepository).save(stock);
        verify(movementRepository).save(any(StockMovement.class));
    }
    @Test
    void reduceStock_insufficientStock_shouldFail() {
        when(stockRepository.findAll()).thenReturn(List.of());

        ReduceStockRequest request =
                new ReduceStockRequest(101L, 5, 99L);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> inventoryService.reduceStock(request)
        );

        assertTrue(ex.getMessage().contains("Insufficient stock"));
    }
}
