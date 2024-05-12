package com.test.orderservice.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class PaymentRequest {
    @NotNull
    private UUID orderId;
}
