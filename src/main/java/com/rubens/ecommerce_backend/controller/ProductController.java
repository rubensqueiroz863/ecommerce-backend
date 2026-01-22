package com.rubens.ecommerce_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.service.ProductService;

@RestController
@RequestMapping("/produtos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 🔍 Buscar produtos por nome
    @GetMapping("/buscar")
    public List<ProductDTO> buscarPorNome(@RequestParam(required = false) String name) {
        return productService.findByName(name);
    }

    // 📦 Buscar produtos por subcategoria (slug)
    @GetMapping("/subcategoria/{slug}")
    public List<ProductDTO> buscarPorSubCategoria(@PathVariable String slug) {
        return productService.findBySubCategorySlug(slug);
    }
}
