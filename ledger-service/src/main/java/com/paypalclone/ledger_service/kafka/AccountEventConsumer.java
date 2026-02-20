package com.paypalclone.ledger_service.kafka;


import com.paypalclone.ledger_service.config.LedgerAccountMapper;
import com.paypalclone.ledger_service.service.LedgerAccountService;
import org.springframework.stereotype.Component;
import com.paypalclone.account.*;
import org.springframework.kafka.annotation.KafkaListener;

@Component
public class AccountEventConsumer {

    private final LedgerAccountService ledgerAccountService;

    public AccountEventConsumer(LedgerAccountService ledgerAccountService) {
        this.ledgerAccountService = ledgerAccountService;
    }

    @KafkaListener(
            topics = "account.account.created",
            containerFactory = "accountCreatedKafkaListenerContainerFactory"
    )
    public void onAccountCreated(AccountCreatedEvent event) {

        ledgerAccountService.createIfNotExists(
                event.getAccountId(),
                LedgerAccountMapper.map(event.getAccountType())
        );
    }


    @KafkaListener(
            topics = "account.account.activated",
            containerFactory = "accountActivatedKafkaListenerContainerFactory"
    )
    public void onAccountActivated(AccountActivatedEvent event) {
        ledgerAccountService.activate(event.getAccountId());
    }

    @KafkaListener(
            topics = "account.account.suspended",
            containerFactory = "accountSuspendedKafkaListenerContainerFactory"
    )
    public void onAccountSuspended(AccountSuspendedEvent event) {
        ledgerAccountService.suspend(event.getAccountId());
    }

    @KafkaListener(
            topics = "account.account.closed",
            containerFactory = "accountClosedKafkaListenerContainerFactory"
    )
    public void onAccountClosed(AccountClosedEvent event) {
        ledgerAccountService.close(event.getAccountId());
    }
}
