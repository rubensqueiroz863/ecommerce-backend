package com.rubens.ecommerce_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.dto.ProductRequestDTO;
import com.rubens.ecommerce_backend.model.Product;
import com.rubens.ecommerce_backend.model.ProductActivityLog;
import com.rubens.ecommerce_backend.model.SubCategory;
import com.rubens.ecommerce_backend.repository.ClickEventRepository;
import com.rubens.ecommerce_backend.repository.ProductActivityLogRepository;
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
    private final ProductActivityLogRepository productActivityLogRepository;

    public ProductDTO createProduct(ProductRequestDTO productDTO, String performedBy) {

        SubCategory subCategory = subCategoryRepository
                .findById(productDTO.subCategory())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Product product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setPhoto(productDTO.photo());
        product.setSubCategory(subCategory);

        Product savedProduct = productRepository.save(product);

        productActivityLogRepository.save(ProductActivityLog.builder()
                .productId(savedProduct.getId())
                .performedBy(performedBy)
                .action("CREATE")
                .details("Producto criado: " + savedProduct.getId())
                .timestamp(LocalDateTime.now())
                .build()
        );

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

    public ProductDTO updateProduct(String id, ProductRequestDTO dto, String performedBy) {

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        StringBuilder details = new StringBuilder();

        if (dto.name() != null && !dto.name().isBlank()) {
            details.append("Nome: ").append(product.getName()).append(" -> ").append(dto.name()).append("; ");
            product.setName(dto.name());
        }

        if (dto.price() != null) {
            details.append("Preço: ").append(product.getPrice()).append(" -> ").append(dto.price()).append("; ");
            product.setPrice(dto.price());
        }

        if (dto.photo() != null && !dto.photo().isBlank()) {
            details.append("Foto: ").append(product.getPhoto()).append(" -> ").append(dto.photo()).append("; ");
            product.setPhoto(dto.photo());
        }

        if (dto.subCategory() != null && !dto.subCategory().isBlank()) {
            SubCategory subCategory = subCategoryRepository
                .findById(dto.subCategory())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));
            details.append("Preço: ").append(product.getSubCategory().getName()).append(" -> ").append(dto.subCategory()).append("; ");
            product.setSubCategory(subCategory);
        }

        Product updatedProduct = productRepository.save(product);

        productActivityLogRepository.save(ProductActivityLog.builder()
                .productId(updatedProduct.getId())
                .performedBy(performedBy)
                .action("UPDATE")
                .details(details.toString())
                .timestamp(LocalDateTime.now())
                .build()
        );

        return toDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(String id, String performedBy) {
        clickEventRepository.deleteByProductId(id);

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        productRepository.delete(product);

        productActivityLogRepository.save(ProductActivityLog.builder()
                .productId(product.getId())
                .performedBy(performedBy)
                .action("DELETE")
                .details("Produto deletado: " + product.getName() + ", preço: " + product.getPrice() + ", subcategory: " + product.getSubCategory().getName())
                .timestamp(LocalDateTime.now())
                .build()
        );
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
