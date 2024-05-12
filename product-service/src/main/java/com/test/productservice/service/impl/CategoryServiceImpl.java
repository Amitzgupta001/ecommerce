package com.test.productservice.service.impl;

import com.test.productservice.common.NullChecker;
import com.test.productservice.dto.request.CategoryRequest;
import com.test.productservice.dto.response.CategoryResponse;
import com.test.productservice.entity.Category;
import com.test.productservice.exception.EntityNotFoundException;
import com.test.productservice.exception.NothingToUpdateException;
import com.test.productservice.mapping.CategoryMapping;
import com.test.productservice.repository.CategoryRepository;
import com.test.productservice.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapping categoryMapping;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        log.info("Started: creating createCategory for categoryRequest: {}", categoryRequest);
        Category savedCategory = categoryRepository.save(categoryMapping.requestToEntity(categoryRequest));
        CategoryResponse categoryResponse = categoryMapping.entityToResponse(savedCategory);
        log.debug("Ended: Ended createCategory for categoryRequest: {} response:{}", categoryRequest, categoryResponse);
        return categoryResponse;
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(UUID categoryId, CategoryRequest categoryRequest) {
        log.info("Started: creating updateCategory for categoryRequest: {}", categoryRequest);
        validateCategory(categoryId, categoryRequest);
        Category existCategory = categoryRepository.findById(categoryId).get();
        Category category = categoryMapping.updateProduct(existCategory, categoryRequest);
        category.setCategoryId(categoryId);
        Category savedCategory = categoryRepository.save(category);
        CategoryResponse categoryResponse = categoryMapping.entityToResponse(savedCategory);
        log.debug("Ended: Ended updateCategory for categoryRequest: {} response:{}", categoryRequest, categoryResponse);
        return categoryResponse;
    }

    @Override
    public List<CategoryResponse> getAllCategory() {
        log.info("Started: fectching getAllCategory ");
        List<Category> savedCategorys = categoryRepository.findAll();
        List<CategoryResponse> categoryResponseList = savedCategorys.stream().map(savedCategory ->
                categoryMapping.entityToResponse(savedCategory)).collect(Collectors.toList());
        log.debug("Ended: Ended getAllCategory for response:{}", categoryResponseList);
        return categoryResponseList;
    }

    private void validateCategory(UUID categoryId, CategoryRequest categoryRequest) {
        boolean isExists = categoryRepository.existsById(categoryId);
        if(!isExists){
            throw new EntityNotFoundException("Category Not Found");
        }

        if(NullChecker.allNull(categoryRequest)){
            throw new NothingToUpdateException("ALl Field is null in request");
        }
    }

}
