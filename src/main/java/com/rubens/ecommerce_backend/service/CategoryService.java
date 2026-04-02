package com.rubens.ecommerce_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.CategoryDTO;
import com.rubens.ecommerce_backend.exception.CategoryFetchException;
import com.rubens.ecommerce_backend.model.Category;
import com.rubens.ecommerce_backend.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> findAll() {

        List<Category> categories;

        try {
            categories = categoryRepository.findAll();
        } catch (Exception e) {
            throw new CategoryFetchException("Categories could not be fetched.");
        }

        return categories.stream()
                .map(this::toDTO)
                .toList();
    }

    private CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
