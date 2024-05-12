package com.test.productservice.mapping;

import com.test.productservice.common.CustomBeanUtil;
import com.test.productservice.dto.request.CategoryRequest;
import com.test.productservice.dto.request.ProductRequest;
import com.test.productservice.dto.response.CategoryResponse;
import com.test.productservice.dto.response.ProductResponse;
import com.test.productservice.entity.Category;
import com.test.productservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.lang.reflect.InvocationTargetException;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapping {

     default Category updateProduct(Category category, CategoryRequest categoryRequest) {
        try {
            CustomBeanUtil.copyPropertiesNotNull(category, categoryRequest);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    Category requestToEntity(CategoryRequest categoryRequest);

    CategoryResponse entityToResponse(Category  category);

}
