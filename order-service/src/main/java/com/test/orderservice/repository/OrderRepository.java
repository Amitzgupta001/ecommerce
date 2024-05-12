package com.test.orderservice.repository;

import com.test.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByOrderIdAndUserId(UUID orderId, UUID userId);

    Optional<Order> findByPaymentIdAndUserId(UUID paymentId, UUID userId);

    List<Order> findAllOrderByUserId(UUID userId);
}
