package com.paypalclone.order_service.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_line_items")
@Getter
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    protected OrderLineItem() {}

    public OrderLineItem(String productId,
                         Integer quantity,
                         BigDecimal unitPrice) {

        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // FACTORY

    public static OrderLineItem create(String productId,
                                       Integer quantity,
                                       BigDecimal unitPrice) {

        return new OrderLineItem(productId, quantity, unitPrice);
    }

    // DOMAIN

    void attachTo(Order order) {
        this.order = order;
    }

    public BigDecimal getSubTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

}
