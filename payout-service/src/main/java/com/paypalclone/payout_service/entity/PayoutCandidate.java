package com.paypalclone.payout_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(
        name = "payout_candidates",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_payout_candidate_payment_intent",
                        columnNames = "payment_intent_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayoutCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_intent_id", nullable = false, updatable = false)
    private Long paymentIntentId;

    @Column(name = "merchant_ledger_account_id", nullable = false)
    private Long merchantLedgerAccountId;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public enum Status {
        PENDING,
        PROCESSED
    }

    private PayoutCandidate(
            Long paymentIntentId,
            Long merchantLedgerAccountId,
            String currency
    ) {
        this.paymentIntentId = paymentIntentId;
        this.merchantLedgerAccountId = merchantLedgerAccountId;
        this.currency = currency;
        this.status = Status.PENDING;
        this.createdAt = Instant.now();
    }

    public static PayoutCandidate create(
            Long paymentIntentId,
            Long merchantLedgerAccountId,
            String currency
    ) {
        return new PayoutCandidate(
                paymentIntentId,
                merchantLedgerAccountId,
                currency
        );
    }

    public void markProcessed() {
        this.status = Status.PROCESSED;
    }
}