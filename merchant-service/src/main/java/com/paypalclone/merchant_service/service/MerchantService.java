package com.paypalclone.merchant_service.service;

import com.paypalclone.merchant_service.entity.Merchant;
import com.paypalclone.merchant_service.kafka.KafkaTopics;
import com.paypalclone.merchant_service.kafka.MerchantEventPublisher;
import com.paypalclone.merchant_service.repository.MerchantRepository;
import com.paypalclone.merchant.MerchantCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantEventPublisher merchantEventPublisher;


    @Transactional
    public Merchant createMerchant(
            Long userId,
            String businessName,
            String businessType,
            String country
    ) {

        return merchantRepository.findByUserId(userId)
                .orElseGet(() -> {

                    Merchant merchant = Merchant.create(
                            userId,
                            businessName,
                            businessType,
                            country
                    );

                    merchantRepository.save(merchant);

                    log.info(
                            "Merchant created: merchantId={}, userId={}",
                            merchant.getMerchantId(),
                            merchant.getUserId()
                    );

                    MerchantCreatedEvent event =
                            MerchantCreatedEvent.builder()
                                    .eventType("MERCHANT_CREATED")
                                    .eventVersion(1)
                                    .merchantId(merchant.getMerchantId())
                                    .userId(merchant.getUserId())
                                    .businessType(merchant.getBusinessType())
                                    .country(merchant.getCountry())
                                    .build();

                    merchantEventPublisher.publish(
                            KafkaTopics.MERCHANT_CREATED,
                            merchant.getUserId().toString(),
                            event
                    );

                    return merchant;
                });
    }

    @Transactional(readOnly = true)
    public Merchant getMerchantForUser(Long userId) {
        return merchantRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Merchant not found for user " + userId
                        )
                );
    }
}
