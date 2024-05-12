package com.test.orderservice.service.impl;

import com.test.orderservice.client.ProductInventoryClient;
import com.test.orderservice.client.UserClient;
import com.test.orderservice.client.productservice.models.*;
import com.test.orderservice.dto.request.CreateOrderRequest;
import com.test.orderservice.dto.request.PaymentRequest;
import com.test.orderservice.dto.request.UpdatePaymentRequest;
import com.test.orderservice.dto.response.OrderResponse;
import com.test.orderservice.dto.response.PaymentResponse;
import com.test.orderservice.entity.Order;
import com.test.orderservice.enums.OrderStatus;
import com.test.orderservice.mapping.OrderMapping;
import com.test.orderservice.repository.OrderRepository;
import com.test.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductInventoryClient productInventoryClient;


    @Autowired
    private OrderMapping orderMapping;
    

    @Override
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest, HttpServletRequest request) {
        
        log.info("Creating order. Request: {}", createOrderRequest);

        ProductResponse productResponse = validateProductAndInventory(createOrderRequest);
        ResponseEntity<CommonResponseUserResponse> userResponse = userClient.getCustomerByToken(request.getHeaders(HttpHeaders.AUTHORIZATION).nextElement());
        if(userResponse.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException("User Not found");
        }
        UserResponse user = Objects.requireNonNull(userResponse.getBody()).getData();
        Order order = Order.builder().productId(productResponse.getProductId())
                .userId(user.getId())
                .productName(productResponse.getProductName())
                .quantity(createOrderRequest.getQuantity())
                .totalMRPPrice(productResponse.getProductMRP().multiply(BigDecimal.valueOf(createOrderRequest.getQuantity())))
                .totalDiscountPre(productResponse.getProductDiscountPer())
                .totalSellingPrice(productResponse.getProductSelling().multiply(BigDecimal.valueOf(createOrderRequest.getQuantity())))
                .status(OrderStatus.PENDING)
                .build();
        Order savedOrder = orderRepository.save(order);

        
        log.info("Order created successfully. Response: {}", savedOrder);

        return orderMapping.entityToResponse(savedOrder);
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest, HttpServletRequest request) {
        
        log.info("Creating payment. Request: {}", paymentRequest);

        ResponseEntity<CommonResponseUserResponse> userResponse = userClient.getCustomerByToken(request.getHeaders(HttpHeaders.AUTHORIZATION).nextElement());
        if(userResponse.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException("User Not found");
        }
        Optional<Order> orderOptional = orderRepository.findByOrderIdAndUserId(paymentRequest.getOrderId(), Objects.requireNonNull(userResponse.getBody())
                .getData().getId());
        if(orderOptional.isEmpty()){
            throw new RuntimeException("Order not found");
        }
        UUID paymentId = UUID.randomUUID();
        Order order = orderOptional.get();
        order.setPaymentId(paymentId);
        order.setStatus(OrderStatus.INTIATE);
        orderRepository.save(order);

        
        PaymentResponse paymentResponse = PaymentResponse.builder().paymentId(paymentId).orderId(order.getOrderId()).price(order.getTotalSellingPrice()).build();
        log.info("Payment created successfully. Response: {}", paymentResponse);
        return paymentResponse;
    }

    @Override
    public void deleteOrder(UUID orderId, HttpServletRequest request) {
        
        log.info("Deleting order. Order ID: {}", orderId);

        ResponseEntity<CommonResponseUserResponse> userResponse = userClient.getCustomerByToken(request.getHeaders(HttpHeaders.AUTHORIZATION).nextElement());
        if(userResponse.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException("User Not found");
        }
        Optional<Order> orderOptional = orderRepository.findByOrderIdAndUserId(orderId, Objects.requireNonNull(userResponse.getBody())
                .getData().getId());
        if(orderOptional.isEmpty()){
            throw new RuntimeException("Order not found");
        }
        orderRepository.delete(orderOptional.get());

        
        log.info("Order deleted successfully.");
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(UpdatePaymentRequest updatePaymentRequest, HttpServletRequest request) {
        
        log.info("Updating order. Request: {}", updatePaymentRequest);

        ResponseEntity<CommonResponseUserResponse> userResponse = userClient.getCustomerByToken(request.getHeaders(HttpHeaders.AUTHORIZATION).nextElement());
        if(userResponse.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException("User Not found");
        }
        Optional<Order> orderOptional = orderRepository.findByPaymentIdAndUserId(updatePaymentRequest.getPaymentID(), Objects.requireNonNull(userResponse.getBody())
                .getData().getId());
        if(orderOptional.isEmpty()){
            throw new RuntimeException("Order not found");
        }
        Order existOrder = orderOptional.get();
        InventoryUpdateRequest inventoryUpdateRequest = new InventoryUpdateRequest().productId(existOrder.getProductId()).quantity(existOrder.getQuantity());

        if(updatePaymentRequest.getStatus() == OrderStatus.SUCCESS){
            ResponseEntity<CommonResponseInventoryResponse> updateInventoryResponse = productInventoryClient.removeInventory(request.getHeaders(HttpHeaders.AUTHORIZATION).nextElement(),
                    inventoryUpdateRequest);
            if(updateInventoryResponse.getStatusCode() != HttpStatus.OK){
                throw new RuntimeException("Error occurred in updating inventory");
            }
        }
        existOrder.setStatus(updatePaymentRequest.getStatus());
        Order updatedOrder = orderRepository.save(existOrder);

        
        log.info("Order updated successfully. Response: {}", updatedOrder);

        return orderMapping.entityToResponse(updatedOrder);
    }

    @Override
    public List<OrderResponse> getAllOrder(HttpServletRequest request) {
        
        log.info("Fetching all orders.");

        ResponseEntity<CommonResponseUserResponse> userResponse = userClient.getCustomerByToken(request.getHeaders(HttpHeaders.AUTHORIZATION).nextElement());
        List<Order> orders = orderRepository.findAllOrderByUserId(Objects.requireNonNull(userResponse.getBody()).getData().getId());

        
        log.info("All orders fetched successfully.");

        return orders.stream().map(order -> orderMapping.entityToResponse(order)).collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(UUID orderId, HttpServletRequest request) {
        
        log.info("Fetching order by ID. Order ID: {}", orderId);

        ResponseEntity<CommonResponseUserResponse> userResponse = userClient.getCustomerByToken(request.getHeaders(HttpHeaders.AUTHORIZATION).nextElement());
        Optional<Order> optionalOrder = orderRepository.findByOrderIdAndUserId(orderId, Objects.requireNonNull(userResponse.getBody()).getData().getId());
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Order Not Found");
        }

        
        log.info("Order fetched successfully by ID. Response: {}", optionalOrder.get());

        return orderMapping.entityToResponse(optionalOrder.get());
    }

    private ProductResponse validateProductAndInventory(CreateOrderRequest createOrderRequest) {
        
        log.info("Validating product and inventory. Request: {}", createOrderRequest);

        ResponseEntity<CommonResponseProductResponse> productDetailRes = productInventoryClient.getProductDetails(createOrderRequest.getProductId());
        ResponseEntity<CommonResponseInventoryResponse> inventoryRes = productInventoryClient.getInventory(createOrderRequest.getProductId());
        if(productDetailRes.getStatusCode() == HttpStatus.OK && inventoryRes.getStatusCode() == HttpStatus.OK){
            InventoryResponse inventory = Objects.requireNonNull(inventoryRes.getBody()).getData();
            if(inventory.getTotalQuantity() < createOrderRequest.getQuantity()) {
                throw new RuntimeException("Not Enough inventory available");
            }else {
                return Objects.requireNonNull(productDetailRes.getBody()).getData();
            }
        }
        throw new RuntimeException("Inventory not found for the product");
    }
}
