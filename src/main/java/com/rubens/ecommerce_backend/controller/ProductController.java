package com.rubens.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.dto.ProductRequestDTO;
import com.rubens.ecommerce_backend.service.ProductService;

@RestController
@RequestMapping("/produtos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Buscar produtos por nome
    @GetMapping("/buscar")
    public PageResponse<ProductDTO> buscarPorNome(
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return productService.findByName(name, page, size);
    }

    // Buscar produtos por subcategoria (slug)
    @GetMapping("/subcategoria/{slug}")
    public PageResponse<ProductDTO> buscarPorSubCategoria(
        @PathVariable String slug,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return productService.findBySubCategorySlug(slug, page, size);
    }

    // Buscar produtos por categoria principal
    @GetMapping("/categoria/{slug}")
    public PageResponse<ProductDTO> buscarPorCategoria(
        @PathVariable String slug,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return productService.findByCategorySlug(slug, page, size);
    }

    @GetMapping("/{id}")
    public ProductDTO buscarPorId(@PathVariable String id) {
        return productService.findById(id);
    }

    @PatchMapping("/edit/{id}")
    public ProductDTO atualizarProduto(
        @PathVariable String id,
        @RequestBody ProductRequestDTO dto
    ) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void deletarProduto(@PathVariable String id) {
        productService.deleteProduct(id);
    }
}
