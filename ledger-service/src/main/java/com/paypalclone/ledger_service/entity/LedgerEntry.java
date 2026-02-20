package com.paypalclone.ledger_service.entity;

import com.paypalclone.ledger_service.enums.LedgerEntryType;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "ledger_entries")
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LedgerTransaction transaction;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LedgerAccount account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerEntryType entryType;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected LedgerEntry() {}

    private LedgerEntry(LedgerTransaction transaction,
                        LedgerAccount account,
                        LedgerEntryType entryType,
                        BigDecimal amount,
                        String currency) {

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        this.transaction = transaction;
        this.account = account;
        this.entryType = entryType;
        this.amount = amount;
        this.currency = currency;
    }

    public static LedgerEntry debit(LedgerTransaction tx,
                                    LedgerAccount account,
                                    BigDecimal amount,
                                    String currency) {
        return new LedgerEntry(tx, account, LedgerEntryType.DEBIT, amount, currency);
    }

    public static LedgerEntry credit(LedgerTransaction tx,
                                     LedgerAccount account,
                                     BigDecimal amount,
                                     String currency) {
        return new LedgerEntry(tx, account, LedgerEntryType.CREDIT, amount, currency);
    }
}