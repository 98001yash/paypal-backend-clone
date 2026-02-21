package com.paypalclone.order_service.entity;


import com.paypalclone.order_service.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // who placed the order
    @Column(nullable = false)
    private Long buyerId;

    // optional use case
    @Column(nullable = true)
    private Long merchantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 3)
    private String currency;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OrderLineItem> lineItems = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Order() {}

    private Order(Long buyerId,
                  Long merchantId,
                  String currency) {

        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.currency = currency;
        this.status = OrderStatus.CREATED;
        this.totalAmount = BigDecimal.ZERO;
        this.createdAt = Instant.now();
    }


    public static Order create(Long buyerId,
                               Long merchantId,
                               String currency) {

        return new Order(buyerId, merchantId, currency);
    }

    // DOMAIN LOGIC

    public void addLineItem(OrderLineItem item) {
        item.attachTo(this);
        this.lineItems.add(item);
        recalculateTotal();
    }

    public void confirm() {
        if (this.status != OrderStatus.CREATED) {
            throw new IllegalStateException("Order cannot be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status != OrderStatus.CREATED) {
            throw new IllegalStateException("Only CREATED orders can be cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }

    private void recalculateTotal() {
        this.totalAmount = lineItems.stream()
                .map(OrderLineItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
