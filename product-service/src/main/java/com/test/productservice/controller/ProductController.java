package com.test.productservice.controller;

import com.test.productservice.dto.request.ProductRequest;
import com.test.productservice.dto.response.CommonResponse;
import com.test.productservice.dto.response.PagingResponse;
import com.test.productservice.dto.response.ProductResponse;
import com.test.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Creates new product", description = "Creates new product", tags = {"product-controller"})
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public CommonResponse<ProductResponse> saveProduct(@RequestBody @Valid ProductRequest productDto) {
        ProductResponse productRes = productService.createProduct(productDto);
        return CommonResponse.<ProductResponse>builder().code(HttpStatus.CREATED.value()).message("Successfully created").data(productRes).build();
    }

    @Operation(summary = "Get product by categoryId ", description = "Get product by categoryId", tags = {"product-controller"})
    @GetMapping("/category/{categoryId}")
    public CommonResponse<PagingResponse<ProductResponse>> getProductByCategoryId(@PathVariable UUID categoryId,
                                                                                    @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                                    @RequestParam(name = "sortBy", defaultValue = "productName") String sortBy,
                                                                                    @RequestParam(name = "sortDir",defaultValue = "asc") String sortDir) {

        return CommonResponse.<PagingResponse<ProductResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Successfully fetched product")
                .data(productService.getProductsByCategoryId(categoryId, pageNo, pageSize, sortBy, sortDir)).build();
    }

    @Operation(summary = "Get product details ", description = "Get product details", tags = {"product-controller"})
    @GetMapping("/{productId}")
    public CommonResponse<ProductResponse> getProductDetails(@PathVariable UUID productId) {
        return CommonResponse.<ProductResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Successfully fetched product")
                .data(productService.getProductById(productId)).build();
    }

    @Operation(summary = "update existing product", description = "update existing product", tags = {"product-controller"})
    @PutMapping("/{productId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public CommonResponse<ProductResponse> updateProduct(@PathVariable UUID productId, @RequestBody ProductRequest productDto) {
        ProductResponse productRes = productService.updateProduct(productId, productDto);
        return CommonResponse.<ProductResponse>builder().code(HttpStatus.OK.value()).message("Successfully updated").data(productRes).build();
    }

}
