package com.test.orderservice.dto.request;

import com.test.orderservice.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UpdatePaymentRequest {
    @NonNull
    private UUID paymentID;

    @NotNull
    private OrderStatus status;
}
