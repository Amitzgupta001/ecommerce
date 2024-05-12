package com.test.orderservice.dto.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CreateOrderRequest {
    @NotNull
    private UUID productId;

    @Min(1)
    @NotNull
    private Integer quantity;
}
