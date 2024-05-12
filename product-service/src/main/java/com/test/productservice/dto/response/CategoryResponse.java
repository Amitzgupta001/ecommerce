package com.test.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CategoryResponse {

    private UUID categoryId;

    private String categoryName;

    private String categoryDescription;

}