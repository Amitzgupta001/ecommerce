package com.test.productservice.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductRequest {

    @NotEmpty
    private String productName;

    @NotNull
    private UUID categoryId;

    @NotEmpty
    private String productDescription;

    @NotEmpty
    private String productBrand;

    @NotEmpty
    private String productPriceCurrency;

    @NotNull
    private BigDecimal productMRP;

    @NotNull
    private BigDecimal productSelling;

    @NotNull
    private BigDecimal productDiscountPer;


}