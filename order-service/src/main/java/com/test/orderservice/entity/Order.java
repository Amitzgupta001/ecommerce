package com.test.orderservice.entity;


import com.test.orderservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
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
