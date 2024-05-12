package com.test.productservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponse {

    private UUID productId;

    private String productName;

    private UUID productCategory;

    private String productDescription;

    private String productImage;

    private String productBrand;

    private boolean productInStock;

    private String productPriceCurrency;

    private BigDecimal productMRP;

    private BigDecimal productSelling;

    private BigDecimal productDiscountPer;

}