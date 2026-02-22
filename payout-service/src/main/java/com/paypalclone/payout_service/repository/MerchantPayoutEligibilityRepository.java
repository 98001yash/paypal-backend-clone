package com.paypalclone.payout_service.repository;

import com.paypalclone.payout_service.entity.MerchantPayoutEligibility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantPayoutEligibilityRepository
        extends JpaRepository<MerchantPayoutEligibility, Long> {
}