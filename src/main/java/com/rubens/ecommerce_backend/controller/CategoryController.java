package com.rubens.ecommerce_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.CategoryDTO;
import com.rubens.ecommerce_backend.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  
  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public List<CategoryDTO> findAll() {
    return categoryService.findAll();
  }
}
