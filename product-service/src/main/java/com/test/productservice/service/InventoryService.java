package com.test.productservice.service;

import com.test.productservice.dto.request.InventoryUpdateRequest;
import com.test.productservice.dto.response.InventoryResponse;

import java.util.UUID;

public interface InventoryService {
    InventoryResponse addInventory(InventoryUpdateRequest inventoryService);

    InventoryResponse removeInventory(InventoryUpdateRequest inventoryService);

    InventoryResponse getInventory(UUID productId);

    void intializeInventory(UUID productId);
}
