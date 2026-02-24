package com.rubens.ecommerce_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.model.Product;
import com.rubens.ecommerce_backend.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PageResponse<ProductDTO> findByName(
        String name,
        int page,
        int size
    ) {
        if (name == null || name.isBlank()) {
            return new PageResponse<>(List.of(), false);
        }

        Page<Product> result =
            productRepository.findByNameContainingIgnoreCase(
                name,
                PageRequest.of(page, size)
            );

        return toPageResponse(result);
    }

    public PageResponse<ProductDTO> findBySubCategorySlug(
        String slug,
        int page,
        int size
    ) {
        Page<Product> result =
            productRepository.findBySubCategory_Slug(
                slug,
                PageRequest.of(page, size)
            );

        return toPageResponse(result);
    }

    public PageResponse<ProductDTO> findByCategorySlug(
        String slug,
        int page,
        int size
    ) {
        Page<Product> result =
            productRepository.findBySubCategory_Category_Slug(
                slug,
                PageRequest.of(page, size)
            );

        return toPageResponse(result);
    }

    public ProductDTO findById(String id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Produto não encontrado")
            );
        return toDTO(product);
    }

    private PageResponse<ProductDTO> toPageResponse(Page<Product> page) {
        return new PageResponse<>(
            page.getContent()
                .stream()
                .map(this::toDTO)
                .toList(),
            page.hasNext()
        );
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
