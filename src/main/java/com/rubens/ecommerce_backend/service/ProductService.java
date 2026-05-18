package com.rubens.ecommerce_backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.dto.ProductRequestDTO;
import com.rubens.ecommerce_backend.dto.StripeProductResponse;
import com.rubens.ecommerce_backend.exception.InvalidCategoryException;
import com.rubens.ecommerce_backend.exception.InvalidLimitException;
import com.rubens.ecommerce_backend.exception.InvalidPageException;
import com.rubens.ecommerce_backend.exception.InvalidProductIdException;
import com.rubens.ecommerce_backend.exception.InvalidProductNameException;
import com.rubens.ecommerce_backend.exception.InvalidProductPriceException;
import com.rubens.ecommerce_backend.exception.InvalidSubCategoryException;
import com.rubens.ecommerce_backend.exception.ProductCreationException;
import com.rubens.ecommerce_backend.exception.ProductDeletionException;
import com.rubens.ecommerce_backend.exception.ProductNotFoundException;
import com.rubens.ecommerce_backend.exception.ProductUpdateException;
import com.rubens.ecommerce_backend.exception.SubCategoryNotFoundException;
import com.rubens.ecommerce_backend.model.Product;
import com.rubens.ecommerce_backend.model.ProductActivityLog;
import com.rubens.ecommerce_backend.model.SubCategory;
import com.rubens.ecommerce_backend.repository.ClickEventRepository;
import com.rubens.ecommerce_backend.repository.ProductActivityLogRepository;
import com.rubens.ecommerce_backend.repository.ProductRepository;
import com.rubens.ecommerce_backend.repository.SubCategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ClickEventRepository clickEventRepository;
    private final ProductActivityLogRepository productActivityLogRepository;
    private final StripeService stripeService;

    public ProductDTO createProduct(ProductRequestDTO dto, String performedBy) {

        if (dto.name() == null || dto.name().isBlank()) {
            throw new InvalidProductNameException();
        }

        if (dto.price() == null || dto.price() <= 0) {
            throw new InvalidProductPriceException();
        }

        if (dto.subCategory() == null || dto.subCategory().isBlank()) {
            throw new SubCategoryNotFoundException();
        }

        SubCategory subCategory = subCategoryRepository
                .findById(dto.subCategory())
                .orElseThrow(SubCategoryNotFoundException::new);

        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setPhoto(dto.photo());
        product.setSubCategory(subCategory);

        StripeProductResponse stripeData;

        try {
            Long priceInCents = Math.round(dto.price() * 100);

            stripeData = stripeService.createProduct(
                    dto.name(),
                    priceInCents
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductCreationException("Erro ao criar produto no Stripe.");
        }

        product.setStripeProductId(stripeData.productId());
        product.setStripePriceId(stripeData.priceId());

        Product saved;

        try {
            saved = productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new ProductCreationException("Erro ao salvar produto.");
        }

        try {
            productActivityLogRepository.save(ProductActivityLog.builder()
                    .productId(saved.getId())
                    .performedBy(performedBy)
                    .action("CREATE")
                    .details("Produto criado: " + saved.getId()
                            + " | StripeId: " + saved.getStripeProductId())
                    .timestamp(LocalDateTime.now())
                    .build()
            );
        } catch (Exception e) {
            System.err.println("Erro ao salvar log: " + e.getMessage());
        }

        return toDTO(saved);
    }

    public PageResponse<ProductDTO> findAllByName(String name, int page, int size) {

        if (name == null || name.isBlank()) {
            throw new InvalidProductNameException();
        }

        Page<Product> result = productRepository.findByNameContainingIgnoreCase(
                name,
                buildPageRequest(page, size)
        );

        return toPageResponse(result);
    }

    public PageResponse<ProductDTO> findBySubCategorySlug(String slug, int page, int size) {

        if (slug == null || slug.isBlank()) {
            throw new InvalidSubCategoryException();
        }

        Page<Product> result = productRepository.findBySubCategory_Slug(
                slug,
                buildPageRequest(page, size)
        );

        return toPageResponse(result);
    }

    public PageResponse<ProductDTO> findByCategorySlug(String slug, int page, int size) {

        if (slug == null || slug.isBlank()) {
            throw new InvalidCategoryException();
        }

        Page<Product> result = productRepository.findBySubCategory_Category_Slug(
                slug,
                buildPageRequest(page, size)
        );

        return toPageResponse(result);
    }

    public ProductDTO findById(String id) {

        if (id == null || id.isBlank()) {
            throw new InvalidProductIdException();
        }

        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        return toDTO(product);
    }

    public ProductDTO updateProduct(String id, ProductRequestDTO dto, String performedBy) {

        if (id == null || id.isBlank()) {
            throw new InvalidProductIdException();
        }

        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        StringBuilder details = new StringBuilder();

        if (dto.name() != null && !dto.name().isBlank()) {

            try {
                stripeService.updateProductName(
                        product.getStripeProductId(),
                        dto.name()
                );
            } catch (Exception e) {
                e.printStackTrace();
                throw new ProductUpdateException("Erro ao atualizar nome no Stripe.");
            }

            details.append("Nome: ")
                    .append(product.getName())
                    .append(" -> ")
                    .append(dto.name())
                    .append("; ");

            product.setName(dto.name());
        }

        if (dto.price() != null) {
            if (dto.price() <= 0) {
                throw new InvalidProductPriceException();
            }

            try {
                Long priceInCents = Math.round(dto.price() * 100);
                if (product.getStripeProductId() == null || product.getStripeProductId().isBlank()) {
                    throw new ProductUpdateException("Produto não está sincronizado com Stripe.");
                }
                String newPriceId = stripeService.createNewPrice(
                        product.getStripeProductId(),
                        priceInCents
                );

                stripeService.updateDefaultPrice(
                        product.getStripeProductId(),
                        newPriceId
                );

                product.setStripePriceId(newPriceId);

            } catch (Exception e) {
                e.printStackTrace();
                throw new ProductUpdateException("Erro ao atualizar preço no Stripe: " + e.getMessage());
            }

            details.append("Preço: ")
                    .append(product.getPrice())
                    .append(" -> ")
                    .append(dto.price())
                    .append("; ");

            product.setPrice(dto.price());
        }

        if (dto.photo() != null && !dto.photo().isBlank()) {
            details.append("Foto: ").append(product.getPhoto()).append(" -> ").append(dto.photo()).append("; ");
            product.setPhoto(dto.photo());
        }

        if (dto.subCategory() != null && !dto.subCategory().isBlank()) {
            SubCategory subCategory = subCategoryRepository
                    .findById(dto.subCategory())
                    .orElseThrow(SubCategoryNotFoundException::new);

            details.append("SubCategory: ")
                    .append(product.getSubCategory().getName())
                    .append(" -> ")
                    .append(subCategory.getName())
                    .append("; ");

            product.setSubCategory(subCategory);
        }

        Product updated;

        try {
            updated = productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new ProductUpdateException("Product could not be updated.");
        }

        try {
            productActivityLogRepository.save(ProductActivityLog.builder()
                    .productId(updated.getId())
                    .performedBy(performedBy)
                    .action("UPDATE")
                    .details(details.toString())
                    .timestamp(LocalDateTime.now())
                    .build()
            );
        } catch (Exception e) {
            System.err.println("Erro ao salvar log: " + e.getMessage());
        }

        return toDTO(updated);
    }

    @Transactional
    public void deleteProduct(String id, String performedBy) {

        if (id == null || id.isBlank()) {
            throw new InvalidProductIdException();
        }

        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        try {
            clickEventRepository.deleteByProductId(id);
            productRepository.delete(product);
        } catch (DataIntegrityViolationException e) {
            throw new ProductDeletionException("Erro ao deletar produto.");
        }

        try {
            productActivityLogRepository.save(ProductActivityLog.builder()
                    .productId(product.getId())
                    .performedBy(performedBy)
                    .action("DELETE")
                    .details("Produto deletado: " + product.getName()
                            + ", preço: " + product.getPrice()
                            + ", subcategory: " + product.getSubCategory().getName())
                    .timestamp(LocalDateTime.now())
                    .build()
            );
        } catch (Exception e) {
            System.err.println("Erro ao salvar log: " + e.getMessage());
        }
    }

    private PageRequest buildPageRequest(int page, int size) {
        if (page < 0) {
            throw new InvalidPageException();
        }

        if (size <= 0 || size > 100) {
            throw new InvalidLimitException();
        }

        return PageRequest.of(page, size);
    }

    private PageResponse<ProductDTO> toPageResponse(Page<Product> page) {
        return new PageResponse<>(
                page.getContent().stream().map(this::toDTO).toList(),
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