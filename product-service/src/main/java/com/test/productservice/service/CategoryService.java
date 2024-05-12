package com.test.productservice.service;

import com.test.productservice.dto.request.CategoryRequest;
import com.test.productservice.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(UUID categoryId, CategoryRequest categoryRequest);

    List<CategoryResponse> getAllCategory();
}
