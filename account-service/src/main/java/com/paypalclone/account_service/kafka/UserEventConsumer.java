package com.paypalclone.account_service.kafka;

import com.paypalclone.user.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventConsumer {

    // later: AccountRepository / WalletRepository

    @KafkaListener(
            topics = "user.user.created",
            groupId = "account-service",
            containerFactory = "userCreatedKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleUserCreated(UserCreatedEvent event) {

        log.info(
                "Received UserCreatedEvent: internalUserId={}, externalAuthId={}",
                event.getUserId(),
                event.getExternalAuthId()
        );

        // TODO (next step):
        // - idempotency check
        // - create USER wallet
    }
}
