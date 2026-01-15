package com.rubens.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rubens.ecommerce_backend.service.ProductService;

import java.util.List;
import com.rubens.ecommerce_backend.model.Product;


@RestController
@RequestMapping("/produtos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/buscar")
    public List<Product> buscar(@RequestParam String name) {
        return productService.findByName(name);
    }
}
