package com.paypalclone.balance_project_service.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Table(
        name = "balance_projection",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ledger_account_id", "currency"})
        }
)
public class BalanceProjection {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ledger_account_id", nullable = false)
    private Long ledgerAccountId;


    @Column(nullable = false)
    private String currency;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal availableBalance;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal pendingBalance;

    @Column(nullable = false)
    private Instant updatedAt;

    protected BalanceProjection() {}

}
