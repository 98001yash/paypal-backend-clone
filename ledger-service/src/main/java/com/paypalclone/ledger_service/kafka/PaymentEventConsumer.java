package com.paypalclone.ledger_service.kafka;

import com.paypalclone.PaymentIntent.PaymentIntentCapturedEvent;
import com.paypalclone.ledger_service.config.SystemLedgerAccounts;
import com.paypalclone.ledger_service.service.LedgerTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final LedgerTransactionService ledgerTransactionService;

    @KafkaListener(
            topics = "payment.intent.captured",
            containerFactory = "paymentIntentCapturedKafkaListenerContainerFactory"
    )
    public void onPaymentCaptured(PaymentIntentCapturedEvent event) {

        log.info(
                "Processing payment capture intentId={}, merchantAccountId={}",
                event.getPaymentIntentId(),
                event.getCreditExternalAccountId()
        );

        ledgerTransactionService.postTransaction(
                "PAYMENT_CAPTURE_" + event.getPaymentIntentId(),
                "PAYMENT",
                event.getPaymentIntentId().toString(),

                //  DEBIT system / customer clearing account
                SystemLedgerAccounts.BANK_CLEARING_ACCOUNT_EXTERNAL_ID,

                //  CREDIT merchant ledger account
                event.getCreditExternalAccountId(),

                event.getAmount(),
                event.getCurrency()
        );
    }
}
