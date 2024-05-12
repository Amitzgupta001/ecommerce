package com.test.productservice.entity;

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
@Table(name = "product", indexes = {
        @Index(name = "product_name_idx", columnList = "productName"),
        @Index(name = "product_productSelling_idx", columnList = "productSelling")
    }
)
public class Product extends CommonEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID productId;

    private String productName;

    private String productDescription;

    private String productImage;

    private String productBrand;

    private String productPriceCurrency;

    private BigDecimal productMRP;

    private BigDecimal productSelling;

    private BigDecimal productDiscountPer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @OneToOne
    private Inventory inventory;
}
