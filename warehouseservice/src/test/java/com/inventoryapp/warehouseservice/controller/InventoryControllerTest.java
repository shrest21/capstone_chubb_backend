package com.inventoryapp.warehouseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryapp.warehouseservice.dto.*;
import com.inventoryapp.warehouseservice.service.InventoryService;
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

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getGlobalStock_success() throws Exception {
        Mockito.when(inventoryService.getGlobalStock(101L))
                .thenReturn(List.of(new GlobalStockResponse("WH1", 10)));

        mockMvc.perform(get("/inventory/stock/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].warehouseCode").value("WH1"))
                .andExpect(jsonPath("$[0].quantity").value(10));
    }

    @Test
    void checkAvailability_success() throws Exception {
        Mockito.when(inventoryService.checkAvailability(101L, 5))
                .thenReturn(new StockAvailableResponse(true, "WH1"));

        mockMvc.perform(get("/inventory/stock/check")
                        .param("productId", "101")
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.warehouseCode").value("WH1"));
    }

    @Test
    void reduceStock_success() throws Exception {
        ReduceStockRequest request =
                new ReduceStockRequest(101L, 2, 99L);

        mockMvc.perform(post("/inventory/stock/reduce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock reduced succesfully"));
    }
}
