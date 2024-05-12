package com.test.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPageResponse {
    private List<ProductResponse> products;
    private int currentPage;
    private long totalItems;
    private int totalPages;
}
