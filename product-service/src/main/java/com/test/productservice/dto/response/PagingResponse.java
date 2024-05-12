package com.test.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PagingResponse<T> {
    private List<T> content;
    private int pageNumber;
    private long totalItems;
    private int totalPages;
}