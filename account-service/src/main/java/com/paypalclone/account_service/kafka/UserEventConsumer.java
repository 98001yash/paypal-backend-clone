package com.paypalclone.account_service.kafka;

import com.paypalclone.account_service.enums.AccountType;
import com.paypalclone.account_service.service.AccountService;
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

    private final AccountService accountService;

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

        //  Create USER wallet (idempotent inside service)
        accountService.createAccount(
                event.getUserId(),
                AccountType.USER_WALLET
        );
    }
}
