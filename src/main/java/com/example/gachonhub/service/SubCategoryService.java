package com.example.gachonhub.service;

import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.category.SubCategoryRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_CONTENT_ID;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository categoryRepository;

    public SubCategory findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID));
    }
}
