package com.rubens.ecommerce_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.model.Product;
import com.rubens.ecommerce_backend.repository.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<Product> findByName(String name) {
    if (name == null || name.isBlank()) {
      return List.of();
    }
    return productRepository.findByNameContainingIgnoreCase(name);
  }
}