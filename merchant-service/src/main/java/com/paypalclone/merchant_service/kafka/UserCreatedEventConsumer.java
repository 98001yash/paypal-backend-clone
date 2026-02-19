package com.paypalclone.merchant_service.kafka;

import com.paypalclone.merchant_service.entity.MerchantUserMapping;
import com.paypalclone.merchant_service.repository.MerchantUserMappingRepository;
import com.paypalclone.user.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedEventConsumer {

    private final MerchantUserMappingRepository mappingRepository;

    @KafkaListener(
            topics = "user.user.created",
            groupId = "merchant-service",
            containerFactory = "userCreatedKafkaListenerContainerFactory"
    )
    @Transactional
    public void handle(UserCreatedEvent event) {

        log.info(
                "Received UserCreatedEvent: authId={}, internalUserId={}",
                event.getExternalAuthId(),
                event.getUserId()
        );

        // idempotency
        if (mappingRepository.existsByExternalAuthId(event.getExternalAuthId())) {
            return;
        }

        mappingRepository.save(
                new MerchantUserMapping(
                        event.getUserId(),
                        event.getExternalAuthId()
                )
        );
    }
}

