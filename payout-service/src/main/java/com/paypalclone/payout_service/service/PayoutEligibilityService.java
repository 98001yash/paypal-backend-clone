package com.paypalclone.payout_service.service;

import com.paypalclone.payout_service.entity.MerchantPayoutEligibility;
import com.paypalclone.payout_service.repository.MerchantPayoutEligibilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PayoutEligibilityService {

    private final MerchantPayoutEligibilityRepository repository;

    public void enable(Long merchantId) {

        repository.findById(merchantId)
                .ifPresentOrElse(
                        MerchantPayoutEligibility::enable,
                        () -> repository.save(
                                MerchantPayoutEligibility.enabled(merchantId)
                        )
                );
    }


    public void disable(Long merchantId) {

        repository.findById(merchantId)
                .ifPresentOrElse(
                        MerchantPayoutEligibility::disable,
                        () -> repository.save(
                                MerchantPayoutEligibility.disabled(merchantId)
                        )
                );
    }


    @Transactional(readOnly = true)
    public boolean isEligible(Long merchantId) {

        return repository.findById(merchantId)
                .map(MerchantPayoutEligibility::isPayoutEnabled)
                .orElse(false);
    }
}