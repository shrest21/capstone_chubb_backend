package com.inventoryapp.productservice.service;

import com.inventoryapp.productservice.dto.ProductRequest;
import com.inventoryapp.productservice.dto.ProductResponse;
import com.inventoryapp.productservice.model.Product;
import com.inventoryapp.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        Product saved = productRepository.save(product);
        return new ProductResponse(saved.getId(),saved.getName(), saved.getDescription(), saved.getBrand(), saved.getPrice());
    }
    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getBrand(), product.getPrice());
    }
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapToResponse).toList();
    }
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id))
            throw new RuntimeException("Product not found");
        productRepository.deleteById(id);
    }
}

