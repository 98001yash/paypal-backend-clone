package com.paypalclone.ledger_service.kafka;

import com.paypalclone.PaymentIntent.PaymentIntentCapturedEvent;
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
                "Processing payment capture intentId={}, debit={}, credit={}",
                event.getPaymentIntentId(),
                event.getDebitExternalAccountId(),
                event.getCreditExternalAccountId()
        );

        ledgerTransactionService.postTransaction(
                "PAYMENT_CAPTURE_" + event.getPaymentIntentId(),
                "PAYMENT",
                event.getPaymentIntentId().toString(),

                // ✅ Ledger now receives correct identifiers
                event.getDebitExternalAccountId(),
                event.getCreditExternalAccountId(),

                event.getAmount(),
                event.getCurrency()
        );
    }
}
