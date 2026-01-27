package com.rubens.ecommerce_backend.service;

import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.SubCategoryDTO;
import com.rubens.ecommerce_backend.model.SubCategory;
import com.rubens.ecommerce_backend.repository.SubCategoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryService(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    public PageResponse<SubCategoryDTO> findAll(int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        Page<SubCategory> result =
            subCategoryRepository.findAll(pageable);

        return toPageResponse(result);
    }

    private PageResponse<SubCategoryDTO> toPageResponse(Page<SubCategory> page) {
        return new PageResponse<>(
            page.getContent()
                .stream()
                .map(this::toDTO)
                .toList(),
            page.hasNext()
        );
    }

    private SubCategoryDTO toDTO(SubCategory subCategory) {
        SubCategoryDTO dto = new SubCategoryDTO();
        dto.setId(subCategory.getId());
        dto.setName(subCategory.getName());
        dto.setSlug(subCategory.getSlug());
        return dto;
    }
}