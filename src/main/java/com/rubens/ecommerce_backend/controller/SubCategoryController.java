package com.rubens.ecommerce_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.SubCategoryDTO;
import com.rubens.ecommerce_backend.service.SubCategoryService;

@RestController
@RequestMapping("/subcategories")
public class SubCategoryController {
  
  private final SubCategoryService subCategoryService;

  public SubCategoryController(SubCategoryService subCategoryService) {
    this.subCategoryService = subCategoryService;
  }

  @GetMapping
  public List<SubCategoryDTO> findAll() {
    return subCategoryService.findAll();
  }
}
