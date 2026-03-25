package com.rubens.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.dto.ProductRequestDTO;
import com.rubens.ecommerce_backend.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

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
        return productService.createProduct(dto);
    }

    // Funcionando
    @PatchMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable("id") String id, @RequestBody ProductRequestDTO dto) {
        return productService.updateProduct(id, dto);
    }

    // Funcionando
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") String id) {
        productService.deleteProduct(id);
    }
}