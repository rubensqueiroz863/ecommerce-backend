package com.rubens.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.SubCategoryDTO;
import com.rubens.ecommerce_backend.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subcategories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    // Funcionando
    @GetMapping
    public PageResponse<SubCategoryDTO> findAll(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return subCategoryService.findAll(page, size);
    }
}
