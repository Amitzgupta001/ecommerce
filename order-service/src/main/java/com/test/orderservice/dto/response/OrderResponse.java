package com.test.orderservice.dto.response;

import com.test.orderservice.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID orderId;

    private UUID userId;

    private UUID productId;

    private String productName;

    private Integer quantity;

    private BigDecimal totalSellingPrice;

    private BigDecimal totalMRPPrice;

    private BigDecimal totalDiscountPre;

    private UUID paymentId;

    private OrderStatus status;

}
