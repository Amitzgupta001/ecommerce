package com.test.orderservice.mapping;

import com.test.orderservice.dto.response.OrderResponse;
import com.test.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapping {

    OrderResponse entityToResponse(Order order);

}
