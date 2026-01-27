package com.rubens.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.PageResponse;
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
    public PageResponse<SubCategoryDTO> findAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return subCategoryService.findAll(page, size);
    }
}
