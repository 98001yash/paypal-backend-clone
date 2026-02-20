package com.paypalclone.ledger_service.entity;


import com.paypalclone.ledger_service.enums.LedgerTransactionStatus;
import com.paypalclone.ledger_service.enums.LedgerTransactionType;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_transactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"idempotency_key"})
})
public class LedgerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerTransactionType type;

    @Column(name = "reference_type", nullable = false)
    private String referenceType;

    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerTransactionStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected LedgerTransaction() {}

    public LedgerTransaction(String idempotencyKey,
                             LedgerTransactionType type,
                             String referenceType,
                             String referenceId) {
        this.idempotencyKey = idempotencyKey;
        this.type = type;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.status = LedgerTransactionStatus.PENDING;
    }

    public void markPosted() {
        this.status = LedgerTransactionStatus.POSTED;
    }

    public Long getId() {
        return id;
    }
}
