package com.paypalclone.account_service.kafka;

import com.paypalclone.account_service.enums.AccountType;
import com.paypalclone.account_service.service.AccountService;
import com.paypalclone.merchant.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantEventConsumer {

    private final AccountService accountService;

    // =====================================================
    // 1️⃣ MERCHANT CREATED
    // =====================================================
    @KafkaListener(
            topics = "merchant.merchant.created",
            groupId = "account-service",
            containerFactory = "merchantCreatedKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleMerchantCreated(MerchantCreatedEvent event) {

        log.info(
                "Received MerchantCreatedEvent: merchantId={}, userId={}",
                event.getMerchantId(),
                event.getUserId()
        );

        // Create merchant accounts (IDEMPOTENT)
        accountService.createAccount(
                event.getMerchantId(),
                AccountType.MERCHANT_WALLET
        );

        accountService.createAccount(
                event.getMerchantId(),
                AccountType.MERCHANT_SETTLEMENT
        );
    }

    // =====================================================
    // 2️⃣ MERCHANT ACTIVATED
    // =====================================================
    @KafkaListener(
            topics = "merchant.merchant.activated",
            groupId = "account-service",
            containerFactory = "merchantActivatedKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleMerchantActivated(MerchantActivatedEvent event) {

        log.info(
                "Received MerchantActivatedEvent: merchantId={}",
                event.getMerchantId()
        );

        accountService.activateAccount(
                event.getMerchantId(),
                AccountType.MERCHANT_WALLET
        );

        accountService.activateAccount(
                event.getMerchantId(),
                AccountType.MERCHANT_SETTLEMENT
        );
    }

    // =====================================================
    // 3️⃣ MERCHANT LIMITED
    // =====================================================
    @KafkaListener(
            topics = "merchant.merchant.limited",
            groupId = "account-service",
            containerFactory = "merchantLimitedKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleMerchantLimited(MerchantLimitedEvent event) {

        log.info(
                "Received MerchantLimitedEvent: merchantId={}",
                event.getMerchantId()
        );

        // Limit = suspend wallet only
        accountService.suspendAccount(
                event.getMerchantId(),
                AccountType.MERCHANT_WALLET
        );
    }


    // 4️ MERCHANT SUSPENDED
    @KafkaListener(
            topics = "merchant.merchant.suspended",
            groupId = "account-service",
            containerFactory = "merchantSuspendedKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleMerchantSuspended(MerchantSuspendedEvent event) {

        log.info(
                "Received MerchantSuspendedEvent: merchantId={}",
                event.getMerchantId()
        );

        accountService.suspendAccount(
                event.getMerchantId(),
                AccountType.MERCHANT_WALLET
        );

        accountService.suspendAccount(
                event.getMerchantId(),
                AccountType.MERCHANT_SETTLEMENT
        );
    }


    // 5️ MERCHANT REJECTED
    @KafkaListener(
            topics = "merchant.merchant.rejected",
            groupId = "account-service",
            containerFactory = "merchantRejectedKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleMerchantRejected(MerchantRejectedEvent event) {

        log.info(
                "Received MerchantRejectedEvent: merchantId={}",
                event.getMerchantId()
        );

        accountService.closeAccount(
                event.getMerchantId(),
                AccountType.MERCHANT_WALLET
        );

        accountService.closeAccount(
                event.getMerchantId(),
                AccountType.MERCHANT_SETTLEMENT
        );
    }
}
