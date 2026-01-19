package com.rubens.ecommerce_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.model.Product;
import com.rubens.ecommerce_backend.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> findByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of();
        }

        return productRepository.findByNameContainingIgnoreCase(name)
            .stream()
            .map(this::toDTO)
            .toList();
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setPhoto(product.getPhoto());

        dto.setSubCategory(product.getSubCategory().getName());
        dto.setCategory(
            product.getSubCategory().getCategory().getName()
        );

        return dto;
    }
}
