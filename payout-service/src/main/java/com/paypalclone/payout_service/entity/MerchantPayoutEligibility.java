package com.paypalclone.payout_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "merchant_payout_eligibility")
@Getter
@NoArgsConstructor
public class MerchantPayoutEligibility {

    @Id
    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "payout_enabled", nullable = false)
    private boolean payoutEnabled;

    private MerchantPayoutEligibility(Long merchantId, boolean payoutEnabled) {
        this.merchantId = merchantId;
        this.payoutEnabled = payoutEnabled;
    }

    public static MerchantPayoutEligibility enabled(Long merchantId) {
        return new MerchantPayoutEligibility(merchantId, true);
    }

    public static MerchantPayoutEligibility disabled(Long merchantId) {
        return new MerchantPayoutEligibility(merchantId, false);
    }

    public void enable() {
        this.payoutEnabled = true;
    }

    public void disable() {
        this.payoutEnabled = false;
    }
}