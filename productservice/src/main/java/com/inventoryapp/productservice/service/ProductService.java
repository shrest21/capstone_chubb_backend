package com.inventoryapp.productservice.service;

import com.inventoryapp.productservice.dto.ProductRequest;
import com.inventoryapp.productservice.dto.ProductResponse;
import com.inventoryapp.productservice.model.Product;
import com.inventoryapp.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}

