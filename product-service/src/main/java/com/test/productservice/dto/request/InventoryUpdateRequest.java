package com.test.productservice.dto.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Data
public class InventoryUpdateRequest {

    @NotNull
    private UUID productId;

    @Min(1)
    private Integer quantity;
}
