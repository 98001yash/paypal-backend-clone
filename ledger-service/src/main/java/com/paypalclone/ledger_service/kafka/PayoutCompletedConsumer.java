package com.paypalclone.ledger_service.kafka;

import com.paypalclone.ledger_service.config.SystemLedgerAccounts;
import com.paypalclone.payout.PayoutCompletedEvent;
import com.paypalclone.ledger_service.service.LedgerTransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayoutCompletedConsumer {

    private final LedgerTransactionService ledgerTransactionService;

    @KafkaListener(
            topics = "payout.payout.completed",
            groupId = "ledger-service",
            containerFactory = "payoutCompletedKafkaListenerContainerFactory"
    )
    public void onPayoutCompleted(PayoutCompletedEvent event) {

        String idempotencyKey =
                "PAYOUT_" + event.getPayoutId();

        log.info(
                "Processing payout ledger debit payoutId={}, accountId={}, amount={}",
                event.getPayoutId(),
                event.getLedgerAccountId(),
                event.getAmount()
        );

        ledgerTransactionService.postTransaction(
                idempotencyKey,
                "PAYOUT",
                event.getPayoutId().toString(),

                // DEBIT merchant
                event.getLedgerAccountId(),

                // CREDIT bank clearing account
                SystemLedgerAccounts.BANK_CLEARING_ACCOUNT_EXTERNAL_ID,

                event.getAmount(),
                event.getCurrency()
        );
    }
}