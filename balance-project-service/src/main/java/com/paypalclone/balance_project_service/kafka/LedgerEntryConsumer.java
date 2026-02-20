package com.paypalclone.balance_project_service.kafka;

import com.paypalclone.balance_project_service.service.BalanceProjectionService;
import com.paypalclone.ledger.LedgerEntryPostedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LedgerEntryConsumer {

    private final BalanceProjectionService balanceService;

    @KafkaListener(
            topics = "ledger.entry.posted",
            containerFactory =
                    "ledgerEntryPostedKafkaListenerContainerFactory"
    )
    public void onLedgerEntryPosted(LedgerEntryPostedEvent event) {

        log.info(
                "Consuming LedgerEntryPostedEvent [accountId={}, type={}, amount={}]",
                event.getLedgerAccountId(),
                event.getEntryType(),
                event.getAmount()
        );

        balanceService.applyLedgerEntry(
                event.getLedgerAccountId(),
                event.getCurrency(),
                event.getEntryType(),
                event.getAmount()
        );
    }
}
