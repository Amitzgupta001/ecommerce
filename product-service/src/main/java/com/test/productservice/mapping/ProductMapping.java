package com.test.productservice.mapping;

import com.test.productservice.common.CustomBeanUtil;
import com.test.productservice.dto.request.CategoryRequest;
import com.test.productservice.dto.request.ProductRequest;
import com.test.productservice.dto.response.ProductResponse;
import com.test.productservice.entity.Category;
import com.test.productservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapping {

    default Product updateProduct(Product product, ProductRequest productRequest) {
        try {
            CustomBeanUtil.copyPropertiesNotNull(product, productRequest);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return product;
    }
    Product requestToEntity(ProductRequest productRequest);

    @Mapping(target = "productCategory",source = "category.categoryId")
    ProductResponse entityToResponse(Product product);

    List<ProductResponse> entityToResponseList(List<Product> products);



}
