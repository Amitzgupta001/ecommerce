package com.test.orderservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PaymentResponse {
    private UUID paymentId;
    private UUID orderId;
    private BigDecimal price;

}
