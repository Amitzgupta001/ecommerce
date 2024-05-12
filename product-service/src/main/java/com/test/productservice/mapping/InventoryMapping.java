package com.test.productservice.mapping;

import com.test.productservice.dto.request.InventoryUpdateRequest;
import com.test.productservice.dto.request.ProductRequest;
import com.test.productservice.dto.response.InventoryResponse;
import com.test.productservice.dto.response.ProductResponse;
import com.test.productservice.entity.Inventory;
import com.test.productservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InventoryMapping {

    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    InventoryResponse entityToResponse(Inventory inventory);

}
