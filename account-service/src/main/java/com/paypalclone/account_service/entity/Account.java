package com.paypalclone.account_service.entity;


import com.paypalclone.account_service.enums.AccountStatus;
import com.paypalclone.account_service.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_owner_account_type",
                        columnNames = {"owner_id", "account_type"}
                )
        },
        indexes = {
                @Index(name = "idx_account_owner", columnList = "owner_id"),
                @Index(name = "idx_account_type", columnList = "account_type")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;


    /**
     * INTERNAL owner id
     * - user.id for user accounts
     * - merchant.id for merchant accounts
     */
    @Column(name = "owner_id", nullable = false, updatable = false)
    private Long ownerId;



    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, updatable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


    public static Account create(Long ownerId, AccountType type) {
        Instant now = Instant.now();

        return Account.builder()
                .ownerId(ownerId)
                .accountType(type)
                .status(AccountStatus.CREATED)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

      // Lifecycle transitions

    public void activate() {
        if (this.status != AccountStatus.CREATED &&
                this.status != AccountStatus.SUSPENDED) {
            throw new IllegalStateException(
                    "Account cannot be activated from " + status
            );
        }
        this.status = AccountStatus.ACTIVE;
        touch();
    }

    public void suspend() {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Only ACTIVE accounts can be suspended"
            );
        }
        this.status = AccountStatus.SUSPENDED;
        touch();
    }

    public void close() {
        this.status = AccountStatus.CLOSED;
        touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

}
