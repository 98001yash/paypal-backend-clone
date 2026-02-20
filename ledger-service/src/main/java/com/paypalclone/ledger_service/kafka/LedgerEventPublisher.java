package com.paypalclone.ledger_service.kafka;


import com.paypalclone.ledger.LedgerEntryPostedEvent;
import com.paypalclone.ledger.LedgerTransactionCompletedEvent;
import com.paypalclone.ledger_service.entity.LedgerEntry;
import com.paypalclone.ledger_service.entity.LedgerTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LedgerEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEntryPosted(LedgerEntry entry) {

        LedgerEntryPostedEvent event =
                LedgerEntryPostedEvent.builder()
                        .ledgerTransactionId(entry.getTransaction().getId())
                        .ledgerAccountId(entry.getAccount().getId())
                        .entryType(entry.getEntryType().name())
                        .amount(entry.getAmount())
                        .currency(entry.getCurrency())
                        .build();

        kafkaTemplate.send(
                "ledger.entry.posted",
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

        kafkaTemplate.send(
                "ledger.transaction.completed",
                transaction.getId().toString(),
                event
        );
    }
}
