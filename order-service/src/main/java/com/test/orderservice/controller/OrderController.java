package com.test.orderservice.controller;

import com.test.orderservice.dto.request.CreateOrderRequest;
import com.test.orderservice.dto.request.PaymentRequest;
import com.test.orderservice.dto.request.UpdatePaymentRequest;
import com.test.orderservice.dto.response.CommonResponse;
import com.test.orderservice.dto.response.OrderResponse;
import com.test.orderservice.dto.response.PaymentResponse;
import com.test.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest request;

    @Operation(summary = "create new order", description = "create new order", tags = {"order-controller"})
    @PostMapping
    public CommonResponse<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        return CommonResponse.<OrderResponse>builder().code(HttpStatus.CREATED.value()).message("Successfully created")
                .data(orderService.createOrder(createOrderRequest, request)).build();
    }

    @Operation(summary = "payment for the order", description = "payment for the order", tags = {"payment-controller"})
    @PostMapping("/payment")
    public CommonResponse<PaymentResponse> makePayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        return CommonResponse.<PaymentResponse>builder().code(HttpStatus.CREATED.value()).message("Successfully created")
                .data(orderService.createPayment(paymentRequest, request)).build();
    }

    @Operation(summary = "update existing order", description = "update existing order", tags = {"order-controller"})
    @DeleteMapping("/{orderId}")
    public CommonResponse<String> makePayment(@PathVariable("orderId") UUID orderId) {
        orderService.deleteOrder(orderId, request);
        return CommonResponse.<String>builder().code(HttpStatus.CREATED.value()).message("Successfully created").data("Successfully deleted").build();
    }

    @Operation(summary = "payment for the order", description = "payment for the order", tags = {"payment-controller"})
    @PostMapping("/payment/webhook")
    public CommonResponse<OrderResponse> updatePayment(@RequestBody @Valid UpdatePaymentRequest updatePaymentRequest ) {
        return CommonResponse.<OrderResponse>builder().code(HttpStatus.CREATED.value()).message("Successfully created")
                .data(orderService.updateOrder(updatePaymentRequest, request)).build();
    }

    @Operation(summary = "Get All Orders", description = "Get All Orders", tags = {"order-controller"})
    @GetMapping
    public CommonResponse<List<OrderResponse>> getAllOrder() {
        return CommonResponse.<List<OrderResponse>>builder().code(HttpStatus.CREATED.value()).message("Successfully fetched")
                .data(orderService.getAllOrder(request)).build();
    }

    @Operation(summary = "Get order by Id", description = "Get order by Id", tags = {"order-controller"})
    @GetMapping("/{orderId}")
    public CommonResponse<OrderResponse> getOrderById(@PathVariable("orderId") UUID orderId) {
        return CommonResponse.<OrderResponse>builder().code(HttpStatus.CREATED.value()).message("Successfully created")
                .data(orderService.getOrderById(orderId,request)).build();
    }
}
