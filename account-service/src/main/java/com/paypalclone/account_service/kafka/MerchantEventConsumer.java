package com.paypalclone.account_service.kafka;

import com.paypalclone.merchant.MerchantActivatedEvent;
import com.paypalclone.merchant.MerchantCreatedEvent;
import com.paypalclone.merchant.MerchantLimitedEvent;
import com.paypalclone.merchant.MerchantRejectedEvent;
import com.paypalclone.merchant.MerchantSuspendedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantEventConsumer {

    // later: AccountRepository

    // 1️MERCHANT CREATED
    @KafkaListener(
            topics = "merchant.merchant.created",
            groupId = "account-service",
            containerFactory = "merchantKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleMerchantCreated(MerchantCreatedEvent event) {

        log.info(
                "Received MerchantCreatedEvent: merchantId={}, userId={}",
                event.getMerchantId(),
                event.getUserId()
        );

        // TODO:
        // - idempotency
        // - create merchant wallet
        // - create settlement account
    }

    // 2️ MERCHANT ACTIVATED
    @KafkaListener(
            topics = "merchant.merchant.activated",
            groupId = "account-service",
            containerFactory = "merchantKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleMerchantActivated(MerchantActivatedEvent event) {

        log.info(
                "Received MerchantActivatedEvent: merchantId={}",
                event.getMerchantId()
        );

        // TODO:
        // - activate all merchant accounts
    }

    // 3️ MERCHANT LIMITED
    @KafkaListener(
            topics = "merchant.merchant.limited",
            groupId = "account-service",
            containerFactory = "merchantKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleMerchantLimited(MerchantLimitedEvent event) {

        log.info(
                "Received MerchantLimitedEvent: merchantId={}",
                event.getMerchantId()
        );

        // TODO:
        // - limit merchant wallet
    }

    // 4️ MERCHANT SUSPENDED
    @KafkaListener(
            topics = "merchant.merchant.suspended",
            groupId = "account-service",
            containerFactory = "merchantKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleMerchantSuspended(MerchantSuspendedEvent event) {

        log.info(
                "Received MerchantSuspendedEvent: merchantId={}",
                event.getMerchantId()
        );

        // TODO:
        // - suspend all merchant accounts
    }

    // 5️ MERCHANT REJECTED
    @KafkaListener(
            topics = "merchant.merchant.rejected",
            groupId = "account-service",
            containerFactory = "merchantKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleMerchantRejected(MerchantRejectedEvent event) {

        log.info(
                "Received MerchantRejectedEvent: merchantId={}",
                event.getMerchantId()
        );

        // TODO:
        // - close all merchant accounts (NO delete)
    }
}
