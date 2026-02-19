package com.paypalclone.merchant_service.entity;

import com.paypalclone.merchant_service.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(
        name = "merchants",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_merchant_user", columnNames = "user_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id", nullable = false, updatable = false)
    private Long merchantId;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_type", nullable = false)
    private String businessType;

    @Column(name = "country", nullable = false, length = 2)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MerchantStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


    private Merchant(
            Long userId,
            String businessName,
            String businessType,
            String country
    ) {
        this.userId = userId;
        this.businessName = businessName;
        this.businessType = businessType;
        this.country = country;
        this.status = MerchantStatus.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }


    public static Merchant create(
            Long userId,
            String businessName,
            String businessType,
            String country
    ) {
        return new Merchant(
                userId,
                businessName,
                businessType,
                country
        );
    }



    public void activate() {
        if (this.status != MerchantStatus.PENDING &&
                this.status != MerchantStatus.LIMITED) {
            throw new IllegalStateException(
                    "Merchant cannot be activated from status: " + status
            );
        }
        this.status = MerchantStatus.ACTIVE;
        touch();
    }

    public void limit() {
        if (this.status != MerchantStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Only ACTIVE merchants can be limited"
            );
        }
        this.status = MerchantStatus.LIMITED;
        touch();
    }

    public void suspend() {
        if (this.status == MerchantStatus.REJECTED) {
            throw new IllegalStateException(
                    "Rejected merchant cannot be suspended"
            );
        }
        this.status = MerchantStatus.SUSPENDED;
        touch();
    }

    public void reject() {
        if (this.status != MerchantStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING merchants can be rejected"
            );
        }
        this.status = MerchantStatus.REJECTED;
        touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }
}
