package com.rubens.ecommerce_backend.service;

import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.SubCategoryDTO;
import com.rubens.ecommerce_backend.exception.InvalidLimitException;
import com.rubens.ecommerce_backend.exception.InvalidPageException;
import com.rubens.ecommerce_backend.exception.SubCategoryFetchException;
import com.rubens.ecommerce_backend.model.SubCategory;
import com.rubens.ecommerce_backend.repository.SubCategoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    public PageResponse<SubCategoryDTO> findAll(int page, int size) {

        if (page < 0) {
            throw new InvalidPageException();
        }

        if (size <= 0 || size > 100) {
            throw new InvalidLimitException();
        }

        PageRequest pageable = PageRequest.of(page, size);

        Page<SubCategory> result;

        try {
            result = subCategoryRepository.findAll(pageable);
        } catch (Exception e) {
            throw new SubCategoryFetchException("SubCategories could not be fetched.");
        }

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