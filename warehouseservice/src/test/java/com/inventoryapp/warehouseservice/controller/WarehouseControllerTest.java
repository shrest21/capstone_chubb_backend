package com.inventoryapp.warehouseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryapp.warehouseservice.dto.*;
import com.inventoryapp.warehouseservice.model.Warehouse;
import com.inventoryapp.warehouseservice.service.WarehouseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WarehouseController.class)
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private WarehouseService warehouseService;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void createWarehouse_success() throws Exception {
        CreateWarehouseRequest request = new CreateWarehouseRequest("WH1", "Bangalore");
        Warehouse warehouse = new Warehouse(1L, "WH1", "Bangalore");
        Mockito.when(warehouseService.createWarehouse(Mockito.any())).thenReturn(warehouse);
        mockMvc.perform(post("/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("WH1"));
    }

    @Test
    void getAllWarehouses_success() throws Exception {
        Mockito.when(warehouseService.getAllWarehouses())
                .thenReturn(List.of(
                        new WarehouseResponse("WH1", "BLR")
                ));

        mockMvc.perform(get("/warehouses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("WH1"));
    }

    @Test
    void getWarehouseStock_success() throws Exception {
        Mockito.when(warehouseService.getWarehouseStock("WH1"))
                .thenReturn(List.of(
                        new WarehouseStockResponse(101L, "Mouse", 10)
                ));

        mockMvc.perform(get("/warehouses/WH1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Mouse"))
                .andExpect(jsonPath("$[0].quantity").value(10));
    }
}