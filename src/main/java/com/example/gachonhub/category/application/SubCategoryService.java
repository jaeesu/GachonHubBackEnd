package com.example.gachonhub.category.application;

import com.example.gachonhub.category.domain.SubCategory;
import com.example.gachonhub.category.domain.SubCategoryRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_CONTENT_ID;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository categoryRepository;

    public SubCategory findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID));
    }
}
