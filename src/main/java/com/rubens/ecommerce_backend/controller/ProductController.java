package com.rubens.ecommerce_backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.dto.ProductRequestDTO;
import com.rubens.ecommerce_backend.service.ProductService;
import com.rubens.ecommerce_backend.service.WebSocketService;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final WebSocketService webSocketService;

    @GetMapping
    public PageResponse<ProductDTO> findAllByName(
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return productService.findAllByName(name, page, size);
    }

    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable String id) {
        return productService.findById(id);
    }

    @GetMapping("/subcategory/{slug}")
    public PageResponse<ProductDTO> findAllBySubCategory(
        @PathVariable String slug,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return productService.findBySubCategorySlug(slug, page, size);
    }

    @GetMapping("/category/{slug}")
    public PageResponse<ProductDTO> findAllByCategory(
        @PathVariable("slug") String slug,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return productService.findByCategorySlug(slug, page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody ProductRequestDTO dto) {

        ProductDTO savedProduct = productService.createProduct(dto, "system");

        try {
            webSocketService.notify(savedProduct.id(), Map.of(
                    "type", "PRODUCT_CREATED",
                    "product", savedProduct
            ));
        } catch (Exception e) {
            System.err.println("Erro ao notificar websocket: " + e.getMessage());
        }

        return savedProduct;
    }

    @PatchMapping("/{id}")
    public ProductDTO updateProduct(
        @PathVariable("id") String id,
        @RequestBody ProductRequestDTO dto
    ) {
        ProductDTO updatedProduct = productService.updateProduct(id, dto, "system");

        try {
            webSocketService.notify(updatedProduct.id(), Map.of(
                    "type", "PRODUCT_UPDATED",
                    "product", updatedProduct
            ));
        } catch (Exception e) {
            System.err.println("Erro ao notificar websocket: " + e.getMessage());
        }

        return updatedProduct;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("id") String id) {

        productService.deleteProduct(id, "system");

        try {
            webSocketService.notify(id, Map.of(
                    "type", "PRODUCT_DELETED",
                    "productId", id
            ));
        } catch (Exception e) {
            System.err.println("Erro ao notificar websocket: " + e.getMessage());
        }
    }
}