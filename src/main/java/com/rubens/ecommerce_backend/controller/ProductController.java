package com.rubens.ecommerce_backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.dto.ProductRequestDTO;
import com.rubens.ecommerce_backend.service.ProductService;
import com.rubens.ecommerce_backend.service.WebSocketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final WebSocketService webSocketService;

    // Funcionando
    @GetMapping
    public PageResponse<ProductDTO> findAllByName(
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return productService.findAllByName(name, page, size);
    }

    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable("id") String id) {
        return productService.findById(id);
    }

    // Funcionando
    @GetMapping("/subcategory/{slug}")
    public PageResponse<ProductDTO> findAllBySlug(
        @PathVariable("slug") String slug,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size) {
        return productService.findBySubCategorySlug(slug, page, size);
    }

    // Funcionando
    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductRequestDTO dto) {
        ProductDTO savedProduct = productService.createProduct(dto, "system");
        
        webSocketService.notify(savedProduct.id(), Map.of(
                "type", "PRODUCT_CREATED",
                "product", savedProduct
        ));
        
        return savedProduct;
    }

    // Funcionando
    @PatchMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable("id") String id, @RequestBody ProductRequestDTO dto    ) {
        ProductDTO updatedProduct = productService.updateProduct(id, dto, "system");
        
        webSocketService.notify(updatedProduct.id(), Map.of(
                "type", "PRODUCT_UPDATED",
                "product", updatedProduct
        ));

        return updatedProduct;
    }

    // Funcionando
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") String id) {
        productService.deleteProduct(id, "system");

        webSocketService.notify(id, Map.of(
                "type", "PRODUCT_DELETED",
                "productId", id
        ));
    }
}