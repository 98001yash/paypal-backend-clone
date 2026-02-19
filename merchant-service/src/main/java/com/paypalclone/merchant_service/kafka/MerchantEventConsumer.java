package com.paypalclone.merchant_service.kafka;

import com.paypalclone.merchant_service.entity.Merchant;
import com.paypalclone.merchant_service.repository.MerchantRepository;

import com.paypalclone.user.UserKycUpdatedEvent;
import com.paypalclone.user.UserRiskUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantEventConsumer {

    private final MerchantRepository merchantRepository;


    // KYC EVENTS
    @KafkaListener(
            topics = "user.user.kyc-updated",
            containerFactory = "userKycKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleUserKycUpdated(UserKycUpdatedEvent event) {

        log.info(
                "Received UserKycUpdatedEvent for userId={}, newLevel={}",
                event.getUserId(),
                event.getNewLevel()
        );

        merchantRepository.findByUserId(event.getUserId())
                .ifPresent(merchant -> {
                    switch (event.getNewLevel()) {
                        case FULL -> activateIfAllowed(merchant);
                        case NONE, BASIC -> {
                            // do nothing → merchant remains PENDING / LIMITED
                        }
                    }
                });
    }

    @KafkaListener(
            topics = "user.user.risk-updated",
            containerFactory = "userRiskKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleUserRiskUpdated(UserRiskUpdatedEvent event) {

        log.info(
                "Received UserRiskUpdatedEvent for userId={}, newState={}",
                event.getUserId(),
                event.getNewState()
        );

        merchantRepository.findByUserId(event.getUserId())
                .ifPresent(merchant -> {
                    switch (event.getNewState()) {
                        case NORMAL -> activateIfAllowed(merchant);
                        case UNDER_REVIEW -> merchant.limit();
                        case HIGH_RISK, FROZEN -> merchant.suspend();
                    }
                });
    }




    // INTERNAL RULE
    private void activateIfAllowed(Merchant merchant) {
        if (merchant.getStatus() == null) return;

        if (merchant.getStatus().name().equals("PENDING")
                || merchant.getStatus().name().equals("LIMITED")) {
            merchant.activate();
        }
    }
}