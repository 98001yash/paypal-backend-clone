package com.paypalclone.payment_intent_service.entity;

import com.paypalclone.payment_intent_service.enums.PaymentIntentStatus;
import com.paypalclone.payment_intent_service.enums.PaymentMethodType;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "payment_intents",
        indexes = {
                @Index(name = "idx_payment_intent_order", columnList = "order_id"),
                @Index(name = "idx_payment_intent_status", columnList = "status")
        }
)
@Getter
public class PaymentIntent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @Column(name = "buyer_id", nullable = false, updatable = false)
    private Long buyerId;

    @Column(name = "merchant_id", nullable = false, updatable = false)
    private Long merchantId;


    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethodType paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentIntentStatus status;


    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;



    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    protected PaymentIntent() {
    }


    public static PaymentIntent create(
            Long orderId,
            Long buyerId,
            Long merchantId,
            BigDecimal amount,
            String currency,
            PaymentMethodType paymentMethod,
            String idempotencyKey
    ) {
        PaymentIntent intent = new PaymentIntent();
        intent.orderId = orderId;
        intent.buyerId = buyerId;
        intent.merchantId = merchantId;
        intent.amount = amount;
        intent.currency = currency;
        intent.paymentMethod = paymentMethod;
        intent.status = PaymentIntentStatus.CREATED;
        intent.idempotencyKey = idempotencyKey;
        return intent;
    }


    public void authorize() {
        ensureStatus(PaymentIntentStatus.CREATED);
        this.status = PaymentIntentStatus.AUTHORIZED;
        touch();
    }

    public void capture() {
        ensureStatus(PaymentIntentStatus.AUTHORIZED);
        this.status = PaymentIntentStatus.CAPTURED;
        touch();
    }

    public void fail(String reason) {
        if (status == PaymentIntentStatus.CAPTURED) {
            throw new IllegalStateException("Cannot fail a captured payment");
        }
        this.status = PaymentIntentStatus.FAILED;
        touch();
    }

    public void cancel() {
        ensureStatus(PaymentIntentStatus.CREATED);
        this.status = PaymentIntentStatus.CANCELLED;
        touch();
    }

    public void refund() {
        ensureStatus(PaymentIntentStatus.CAPTURED);
        this.status = PaymentIntentStatus.REFUNDED;
        touch();
    }

    // -GUARDS

    private void ensureStatus(PaymentIntentStatus expected) {
        if (this.status != expected) {
            throw new IllegalStateException(
                    "Invalid state transition: " + status + " → " + expected
            );
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }
}
