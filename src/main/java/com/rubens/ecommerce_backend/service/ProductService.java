package com.rubens.ecommerce_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.dto.ProductRequestDTO;
import com.rubens.ecommerce_backend.model.Product;
import com.rubens.ecommerce_backend.model.SubCategory;
import com.rubens.ecommerce_backend.repository.ClickEventRepository;
import com.rubens.ecommerce_backend.repository.ProductRepository;
import com.rubens.ecommerce_backend.repository.SubCategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ClickEventRepository clickEventRepository;
    
    public ProductDTO createProduct(ProductRequestDTO productDTO) {

        SubCategory subCategory = subCategoryRepository
                .findById(productDTO.subCategory())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Product product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setPhoto(productDTO.photo());
        product.setSubCategory(subCategory);

        Product savedProduct = productRepository.save(product);

        return new ProductDTO(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getPhoto(),
                savedProduct.getSubCategory().getId()
        );
    }

    public PageResponse<ProductDTO> findAllByName(
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

    public ProductDTO updateProduct(String id, ProductRequestDTO dto) {

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (dto.name() != null && !dto.name().isBlank()) {
            product.setName(dto.name());
        }

        if (dto.price() != null) {
            product.setPrice(dto.price());
        }

        if (dto.photo() != null && !dto.photo().isBlank()) {
            product.setPhoto(dto.photo());
        }

        if (dto.subCategory() != null && !dto.subCategory().isBlank()) {
            SubCategory subCategory = subCategoryRepository
                .findById(dto.subCategory())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

            product.setSubCategory(subCategory);
        }

        Product updatedProduct = productRepository.save(product);

        return toDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(String id) {
        clickEventRepository.deleteByProductId(id);

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        productRepository.delete(product);
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
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getPhoto(),
            product.getSubCategory().getName()
        );
    }
}
