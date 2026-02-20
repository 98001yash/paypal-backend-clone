package com.paypalclone.ledger_service.entity;

import com.paypalclone.ledger_service.enums.LedgerAccountStatus;
import com.paypalclone.ledger_service.enums.LedgerAccountType;
import jakarta.persistence.*;

import java.time.Instant;
@Entity
@Table(
        name = "ledger_accounts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"external_account_id"})
        }
)
public class LedgerAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_account_id", nullable = false, unique = true)
    private Long externalAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerAccountType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerAccountStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected LedgerAccount() {}

    private LedgerAccount(Long externalAccountId,
                          LedgerAccountType type) {
        this.externalAccountId = externalAccountId;
        this.type = type;
        this.status = LedgerAccountStatus.ACTIVE;
    }

    public static LedgerAccount create(Long externalAccountId,
                                       LedgerAccountType type) {
        return new LedgerAccount(externalAccountId, type);
    }

    public void activate() {
        this.status = LedgerAccountStatus.ACTIVE;
    }

    public void block() {
        this.status = LedgerAccountStatus.BLOCKED;
    }
}
