package com.test.orderservice.service;

import com.test.orderservice.dto.request.CreateOrderRequest;
import com.test.orderservice.dto.request.PaymentRequest;
import com.test.orderservice.dto.request.UpdatePaymentRequest;
import com.test.orderservice.dto.response.OrderResponse;
import com.test.orderservice.dto.response.PaymentResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest createOrderRequest, HttpServletRequest request);

    PaymentResponse createPayment(PaymentRequest paymentRequest, HttpServletRequest request);

    void deleteOrder(UUID orderId, HttpServletRequest request);

    OrderResponse updateOrder(UpdatePaymentRequest updatePaymentRequest, HttpServletRequest request);

    List<OrderResponse> getAllOrder(HttpServletRequest request);

    OrderResponse getOrderById(UUID orderId, HttpServletRequest request);
}
