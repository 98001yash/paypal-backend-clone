package com.paypalclone.ledger_service.service;


import com.paypalclone.ledger_service.entity.LedgerAccount;
import com.paypalclone.ledger_service.entity.LedgerEntry;
import com.paypalclone.ledger_service.entity.LedgerTransaction;
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

        // 1️ Idempotency
        if (transactionRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
            return;
        }

        // Load ledger accounts
        LedgerAccount debitAccount =
                accountRepository.findByExternalAccountId(debitAccountExternalId)
                        .orElseThrow(() ->
                                new IllegalStateException("Debit ledger account not found"));

        LedgerAccount creditAccount =
                accountRepository.findByExternalAccountId(creditAccountExternalId)
                        .orElseThrow(() ->
                                new IllegalStateException("Credit ledger account not found"));

        // 3️ Create transaction
        LedgerTransaction transaction =
                transactionRepository.save(
                        LedgerTransaction.create(
                                idempotencyKey,
                                referenceType,
                                referenceId
                        )
                );

        // 4️ Double-entry (atomic)
        LedgerEntry debit =
                LedgerEntry.debit(transaction, debitAccount, amount, currency);

        LedgerEntry credit =
                LedgerEntry.credit(transaction, creditAccount, amount, currency);

        entryRepository.save(debit);
        entryRepository.save(credit);

        // 5⃣ Mark transaction as posted
        transaction.markPosted();
    }
}