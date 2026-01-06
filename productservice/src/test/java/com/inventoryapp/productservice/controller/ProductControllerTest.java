package com.inventoryapp.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryapp.productservice.dto.ProductRequest;
import com.inventoryapp.productservice.dto.ProductResponse;
import com.inventoryapp.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;
    // create
    @Test
    void createProduct_success() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Keyboard");
        request.setDescription("Mechanical Keyboard");
        request.setBrand("Logitech");
        request.setPrice(new BigDecimal("2499.00"));
        ProductResponse response = new ProductResponse(
                1L,
                "Keyboard",
                "Mechanical Keyboard",
                "Logitech",
                new BigDecimal("2499.00")
        );

        Mockito.when(productService.createProduct(Mockito.any(ProductRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Keyboard"))
                .andExpect(jsonPath("$.brand").value("Logitech"));
    }
    // get all products
    @Test
    void getAllProducts_success() throws Exception {

        List<ProductResponse> products = List.of(
                new ProductResponse(1L, "Mouse", "Wireless", "Logitech", new BigDecimal("799")),
                new ProductResponse(2L, "Keyboard", "Mechanical", "HP", new BigDecimal("1999"))
        );

        Mockito.when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Mouse"));
    }
    //get product by id
    @Test
    void getProductById_success() throws Exception {
        ProductResponse response = new ProductResponse(
                1L,
                "Mouse",
                "Wireless",
                "Logitech",
                new BigDecimal("799")
        );
        Mockito.when(productService.getProductById(1L)).thenReturn(response);
        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mouse"))
                .andExpect(jsonPath("$.price").value(799));
    }
    //update
    @Test
    void updateProduct_success() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Mouse Pro");
        request.setDescription("Updated");
        request.setBrand("Logitech");
        request.setPrice(new BigDecimal("999"));
        ProductResponse response = new ProductResponse(
                1L,
                "Mouse Pro",
                "Updated",
                "Logitech",
                new BigDecimal("999")
        );
        Mockito.when(productService.updateProduct(Mockito.eq(1L), Mockito.any(ProductRequest.class)))
                .thenReturn(response);
        mockMvc.perform(put("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mouse Pro"));
    }
    //delete product
    @Test
    void deleteProduct_success() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(1L);
        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isOk());
    }
}