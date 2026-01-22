package com.rubens.ecommerce_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.SubCategoryDTO;
import com.rubens.ecommerce_backend.model.SubCategory;
import com.rubens.ecommerce_backend.repository.SubCategoryRepository;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryService(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    public List<SubCategoryDTO> findAll() {
        return subCategoryRepository.findAll()
            .stream()
            .map(this::toDTO)
            .toList();
    }

    private SubCategoryDTO toDTO(SubCategory subCategory) {
        SubCategoryDTO dto = new SubCategoryDTO();
        dto.setId(subCategory.getId());
        dto.setName(subCategory.getName());
        return dto;
    }
}
