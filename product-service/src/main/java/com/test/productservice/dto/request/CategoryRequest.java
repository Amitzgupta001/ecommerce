package com.test.productservice.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
public class CategoryRequest {

    @NotEmpty
    private String categoryName;

    @NotEmpty
    private String categoryDescription;

}
