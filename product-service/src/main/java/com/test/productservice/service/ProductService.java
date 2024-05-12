package com.test.productservice.service;

import com.test.productservice.dto.request.ProductRequest;
import com.test.productservice.dto.response.PagingResponse;
import com.test.productservice.dto.response.ProductResponse;

import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productDto);

    PagingResponse<ProductResponse> getProductsByCategoryId(UUID categoryId, Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    ProductResponse getProductById(UUID productId);

    ProductResponse updateProduct(UUID productId, ProductRequest productDto);
}
