package com.test.productservice.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class InventoryResponse {
    private UUID productId;

    private String productName;

    private Integer totalQuantity;
}
