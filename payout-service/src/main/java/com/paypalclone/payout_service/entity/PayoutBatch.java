package com.paypalclone.payout_service.entity;


import com.paypalclone.payout_service.enums.PayoutBatchStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Table(
        name = "payout_batches",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"batch_key"})
        }
)
public class PayoutBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_key", nullable = false, unique = true)
    private String batchKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayoutBatchStatus status;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    protected PayoutBatch() {}

    public static PayoutBatch create(
            String batchKey,
            BigDecimal totalAmount,
            String currency
    ) {
        PayoutBatch batch = new PayoutBatch();
        batch.batchKey = batchKey;
        batch.totalAmount = totalAmount;
        batch.currency = currency;
        batch.status = PayoutBatchStatus.CREATED;
        return batch;
    }

    public void markSentToBank() {
        ensureStatus(PayoutBatchStatus.CREATED);
        this.status = PayoutBatchStatus.SENT_TO_BANK;
        touch();
    }

    public void complete() {
        ensureStatus(PayoutBatchStatus.SENT_TO_BANK);
        this.status = PayoutBatchStatus.COMPLETED;
        touch();
    }

    public void fail() {
        this.status = PayoutBatchStatus.FAILED;
        touch();
    }

    private void ensureStatus(PayoutBatchStatus expected) {
        if (this.status != expected) {
            throw new IllegalStateException(
                    "Invalid batch transition: " + status + " → " + expected
            );
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }
}
