package com.test.productservice.controller;


import com.test.productservice.dto.request.CategoryRequest;
import com.test.productservice.dto.request.InventoryUpdateRequest;
import com.test.productservice.dto.response.CategoryResponse;
import com.test.productservice.dto.response.CommonResponse;
import com.test.productservice.dto.response.InventoryResponse;
import com.test.productservice.service.CategoryService;
import com.test.productservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "add new category", description = "add new category", tags = {"category-controller"})
    @PostMapping
    //@SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public CommonResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        return CommonResponse.<CategoryResponse>builder().code(HttpStatus.CREATED.value()).message("Successfully created").data(categoryResponse).build();
    }

    @Operation(summary = "update category", description = "update category", tags = {"category-controller"})
    @PutMapping("/{categoryId}")
    //@SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public CommonResponse<CategoryResponse> updateCategory(@PathVariable UUID categoryId, @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.updateCategory(categoryId, categoryRequest);
        return CommonResponse.<CategoryResponse>builder().code(HttpStatus.OK.value()).message("Successfully updated").data(categoryResponse).build();
    }

    @Operation(summary = "getAll category", description = "get ALl category", tags = {"category-controller"})
    @GetMapping
    public CommonResponse<List<CategoryResponse>> getAllCategory() {
        List<CategoryResponse> categoryResponseList = categoryService.getAllCategory();
        return CommonResponse.<List<CategoryResponse>>builder().code(HttpStatus.OK.value()).message("Successfully fetched").data(categoryResponseList).build();
    }
}
