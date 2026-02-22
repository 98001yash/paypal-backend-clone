package com.paypalclone.payout_service.entity;


import com.paypalclone.payout_service.enums.PayoutStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Table(
        name = "payouts",
        indexes = {
                @Index(name = "idx_payout_merchant", columnList = "merchant_id"),
                @Index(name = "idx_payout_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"idempotency_key"})
        }
)
public class Payout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "merchant_id", nullable = false, updatable = false)
    private Long merchantId;

    @Column(name = "ledger_account_id", nullable = false, updatable = false)
    private Long ledgerAccountId;


    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayoutStatus status;



    @Column(name = "batch_id")
    private String batchId;


    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;


    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    protected Payout() {}



    public static Payout create(
            Long merchantId,
            Long ledgerAccountId,
            BigDecimal amount,
            String currency,
            String idempotencyKey
    ) {
        Payout payout = new Payout();
        payout.merchantId = merchantId;
        payout.ledgerAccountId = ledgerAccountId;
        payout.amount = amount;
        payout.currency = currency;
        payout.status = PayoutStatus.CREATED;
        payout.idempotencyKey = idempotencyKey;
        return payout;
    }


    public void queue(String batchId) {
        ensureStatus(PayoutStatus.CREATED);
        this.batchId = batchId;
        this.status = PayoutStatus.QUEUED;
        touch();
    }

    public void markSentToBank() {
        ensureStatus(PayoutStatus.QUEUED);
        this.status = PayoutStatus.SENT_TO_BANK;
        touch();
    }

    public void complete() {
        ensureStatus(PayoutStatus.SENT_TO_BANK);
        this.status = PayoutStatus.COMPLETED;
        touch();
    }

    public void fail() {
        if (status == PayoutStatus.COMPLETED) {
            throw new IllegalStateException("Completed payout cannot fail");
        }
        this.status = PayoutStatus.FAILED;
        touch();
    }


    private void ensureStatus(PayoutStatus expected) {
        if (this.status != expected) {
            throw new IllegalStateException(
                    "Invalid payout transition: " + status + " → " + expected
            );
        }
    }
    private void touch() {
        this.updatedAt = Instant.now();
    }
}
