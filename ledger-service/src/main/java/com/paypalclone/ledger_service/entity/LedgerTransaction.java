package com.paypalclone.ledger_service.entity;


import com.paypalclone.ledger_service.enums.LedgerTransactionStatus;
import com.paypalclone.ledger_service.enums.LedgerTransactionType;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "ledger_transactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"idempotency_key"})
        }
)
public class LedgerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private String referenceType;

    @Column(nullable = false)
    private String referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerTransactionStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected LedgerTransaction() {}

    private LedgerTransaction(String idempotencyKey,
                              String referenceType,
                              String referenceId) {
        this.idempotencyKey = idempotencyKey;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.status = LedgerTransactionStatus.PENDING;
    }

    public static LedgerTransaction create(String idempotencyKey,
                                           String referenceType,
                                           String referenceId) {
        return new LedgerTransaction(idempotencyKey, referenceType, referenceId);
    }

    public void markPosted() {
        this.status = LedgerTransactionStatus.POSTED;
    }

    public Long getId() {
        return id;
    }
}