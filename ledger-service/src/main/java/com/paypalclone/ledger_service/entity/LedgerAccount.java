package com.paypalclone.ledger_service.entity;


import com.paypalclone.ledger_service.enums.LedgerAccountStatus;
import com.paypalclone.ledger_service.enums.LedgerAccountType;
import jakarta.persistence.*;

import java.time.Instant;


@Entity
@Table(name = "ledger_accounts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"external_account_id", "currency"})
})
public class LedgerAccount {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_account_id", nullable = false)
    private Long externalAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerAccountType type;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerAccountStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected LedgerAccount() {}

    public LedgerAccount(Long externalAccountId,
                         LedgerAccountType type,
                         String currency) {
        this.externalAccountId = externalAccountId;
        this.type = type;
        this.currency = currency;
        this.status = LedgerAccountStatus.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public Long getExternalAccountId() {
        return externalAccountId;
    }

    public LedgerAccountStatus getStatus() {
        return status;
    }

    public void block() {
        this.status = LedgerAccountStatus.BLOCKED;
    }
}
