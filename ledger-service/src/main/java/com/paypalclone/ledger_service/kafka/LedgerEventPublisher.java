package com.paypalclone.ledger_service.kafka;


import com.paypalclone.ledger.LedgerEntryPostedEvent;
import com.paypalclone.ledger.LedgerTransactionCompletedEvent;
import com.paypalclone.ledger_service.entity.LedgerEntry;
import com.paypalclone.ledger_service.entity.LedgerTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class LedgerEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEntryPosted(LedgerEntry entry) {

        LedgerEntryPostedEvent event =
                LedgerEntryPostedEvent.builder()
                        .ledgerTransactionId(entry.getTransaction().getId())
                        .ledgerAccountId(entry.getAccount().getExternalAccountId())
                        .entryType(entry.getEntryType().name())
                        .amount(entry.getAmount())
                        .currency(entry.getCurrency())
                        .build();

        log.info(
                "Publishing LedgerEntryPostedEvent [txId={}, accountId={}, type={}, amount={}]",
                event.getLedgerTransactionId(),
                event.getLedgerAccountId(),
                event.getEntryType(),
                event.getAmount()
        );

        kafkaTemplate.send(
                KafkaTopics.LEDGER_ENTRY_POSTED,
                event.getLedgerAccountId().toString(),
                event
        );

    }

    public void publishTransactionCompleted(LedgerTransaction transaction) {

        LedgerTransactionCompletedEvent event =
                LedgerTransactionCompletedEvent.builder()
                        .ledgerTransactionId(transaction.getId())
                        .referenceType(transaction.getReferenceType())
                        .referenceId(transaction.getReferenceId())
                        .build();

        log.info(
                "Publishing LedgerTransactionCompletedEvent [txId={}, refType={}, refId={}]",
                event.getLedgerTransactionId(),
                event.getReferenceType(),
                event.getReferenceId()
        );

        kafkaTemplate.send(
                KafkaTopics.LEDGER_TRANSACTION_COMPLETED,
                transaction.getId().toString(),
                event
        );
    }
}
