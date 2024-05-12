package com.test.productservice.controller;


import com.test.productservice.dto.request.InventoryUpdateRequest;
import com.test.productservice.dto.response.CommonResponse;
import com.test.productservice.dto.response.InventoryResponse;
import com.test.productservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Operation(summary = "add product inventory", description = "add product inventory", tags = {"inventory-controller"})
    @PostMapping("/add")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public CommonResponse<InventoryResponse> addInventory(@RequestBody @Valid InventoryUpdateRequest inventoryUpdateRequest, @RequestHeader(name = "Authorization") String aut) {
        InventoryResponse addInventoryRes = inventoryService.addInventory(inventoryUpdateRequest);
        return CommonResponse.<InventoryResponse>builder().code(HttpStatus.OK.value()).message("Successfully updated").data(addInventoryRes).build();
    }

    @Operation(summary = "remove product inventory", description = "remove product inventory", tags = {"inventory-controller"})
    @PostMapping("/remove")
    @SecurityRequirement(name = "Bearer Authentication")
    public CommonResponse<InventoryResponse> removeInventory(@RequestBody @Valid InventoryUpdateRequest inventoryUpdateRequest) {
        InventoryResponse addInventoryRes = inventoryService.removeInventory(inventoryUpdateRequest);
        return CommonResponse.<InventoryResponse>builder().code(HttpStatus.OK.value()).message("Successfully updated").data(addInventoryRes).build();
    }

    @Operation(summary = "get product inventory", description = "get product inventory", tags = {"inventory-controller"})
    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public CommonResponse<InventoryResponse> getInventory(@RequestParam("productId") UUID productId) {
        InventoryResponse addInventoryRes = inventoryService.getInventory(productId);
        return CommonResponse.<InventoryResponse>builder().code(HttpStatus.OK.value()).message("Successfully fetched").data(addInventoryRes).build();
    }
}
