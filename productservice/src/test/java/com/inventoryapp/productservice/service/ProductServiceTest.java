package com.inventoryapp.productservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.inventoryapp.productservice.dto.ProductRequest;
import com.inventoryapp.productservice.dto.ProductResponse;
import com.inventoryapp.productservice.model.Product;
import com.inventoryapp.productservice.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;
    private ProductRequest request;
    private Product product;
    @BeforeEach
    void setup() {
        request = new ProductRequest();
        request.setName("Mouse");
        request.setDescription("Wireless Mouse");
        request.setBrand("Logitech");
        request.setPrice(new BigDecimal("799.00"));
        product = new Product(1L, "Mouse", "Wireless Mouse", "Logitech", new BigDecimal("799.00")
        );
    }
    // ---------- CREATE PRODUCT ----------
    @Test
    void createProduct_success() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals("Mouse", response.getName());
        assertEquals("Logitech", response.getBrand());
        assertEquals(new BigDecimal("799.00"), response.getPrice());

        verify(productRepository, times(1)).save(any(Product.class));
    }
    // ---------- GET ALL PRODUCTS ----------
    @Test
    void getAllProducts_success() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponse> responses = productService.getAllProducts();

        assertEquals(1, responses.size());
        assertEquals("Mouse", responses.get(0).getName());
    }
    // ---------- GET PRODUCT BY ID ----------
    @Test
    void getProductById_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        assertEquals("Mouse", response.getName());
        assertEquals("Logitech", response.getBrand());
    }

    @Test
    void getProductById_notFound_shouldFail() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
        assertEquals("Product not found", ex.getMessage());
    }
    //update
    @Test
    void updateProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductResponse response = productService.updateProduct(1L, request);
        assertEquals("Mouse", response.getName());
        verify(productRepository).save(product);
    }
    @Test
    void updateProduct_notFound_shouldFail() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.updateProduct(1L, request));
        assertEquals("Product not found", ex.getMessage());
    }
    //delete
    @Test
    void deleteProduct_success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.deleteProduct(1L);
        verify(productRepository).deleteById(1L);
    }
    @Test
    void deleteProduct_notFound_shouldFail() {
        when(productRepository.existsById(1L)).thenReturn(false);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.deleteProduct(1L));
        assertEquals("Product not found", ex.getMessage());
    }
}