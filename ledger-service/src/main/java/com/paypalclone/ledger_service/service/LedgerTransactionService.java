package com.paypalclone.ledger_service.service;


import com.paypalclone.ledger_service.entity.LedgerAccount;
import com.paypalclone.ledger_service.entity.LedgerEntry;
import com.paypalclone.ledger_service.entity.LedgerTransaction;
import com.paypalclone.ledger_service.kafka.LedgerEventPublisher;
import com.paypalclone.ledger_service.repository.LedgerAccountRepository;
import com.paypalclone.ledger_service.repository.LedgerEntryRepository;
import com.paypalclone.ledger_service.repository.LedgerTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LedgerTransactionService {

    private final LedgerTransactionRepository transactionRepository;
    private final LedgerEntryRepository entryRepository;
    private final LedgerAccountRepository accountRepository;
    private final LedgerEventPublisher eventPublisher;

    @Transactional
    public void postTransaction(
            String idempotencyKey,
            String referenceType,
            String referenceId,
            Long debitAccountExternalId,
            Long creditAccountExternalId,
            BigDecimal amount,
            String currency
    ) {

        if (transactionRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
            return;
        }

        LedgerAccount debitAccount =
                accountRepository.findByExternalAccountId(debitAccountExternalId)
                        .orElseThrow(() -> new IllegalStateException("Debit ledger account not found"));

        LedgerAccount creditAccount =
                accountRepository.findByExternalAccountId(creditAccountExternalId)
                        .orElseThrow(() -> new IllegalStateException("Credit ledger account not found"));

        LedgerTransaction transaction =
                transactionRepository.save(
                        LedgerTransaction.create(
                                idempotencyKey,
                                referenceType,
                                referenceId
                        )
                );

        LedgerEntry debit =
                LedgerEntry.debit(transaction, debitAccount, amount, currency);

        LedgerEntry credit =
                LedgerEntry.credit(transaction, creditAccount, amount, currency);

        entryRepository.save(debit);
        entryRepository.save(credit);

        transaction.markPosted();

        eventPublisher.publishEntryPosted(debit);
        eventPublisher.publishEntryPosted(credit);
        eventPublisher.publishTransactionCompleted(transaction);
    }
}
