package com.paypalclone.merchant_service.service;

import com.paypalclone.merchant_service.entity.Merchant;
import com.paypalclone.merchant_service.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    @Transactional
    public Merchant createMerchant(
            Long userId,
            String businessName,
            String businessType,
            String country
    ) {
        return merchantRepository.findByUserId(userId)
                .orElseGet(() -> merchantRepository.save(
                        Merchant.create(
                                userId,
                                businessName,
                                businessType,
                                country
                        )
                ));
    }

    @Transactional(readOnly = true)
    public Merchant getMerchantForUser(Long userId) {
        return merchantRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalStateException("Merchant not found for user " + userId)
                );
    }
}
