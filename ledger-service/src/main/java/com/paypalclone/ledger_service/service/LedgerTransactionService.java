package com.paypalclone.ledger_service.service;

import com.paypalclone.ledger_service.entity.LedgerAccount;
import com.paypalclone.ledger_service.entity.LedgerEntry;
import com.paypalclone.ledger_service.entity.LedgerTransaction;
import com.paypalclone.ledger_service.kafka.LedgerEventPublisher;
import com.paypalclone.ledger_service.repository.LedgerAccountRepository;
import com.paypalclone.ledger_service.repository.LedgerEntryRepository;
import com.paypalclone.ledger_service.repository.LedgerTransactionRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerTransactionService {

    private final LedgerTransactionRepository transactionRepository;
    private final LedgerEntryRepository entryRepository;
    private final LedgerAccountRepository accountRepository;
    private final LedgerEventPublisher eventPublisher;


    @Transactional
    @CircuitBreaker(name = "ledgerServiceCB", fallbackMethod = "fallback")
    @Retry(name = "ledgerServiceRetry")
    public void postTransaction(
            String idempotencyKey,
            String referenceType,
            String referenceId,
            Long debitAccountExternalId,
            Long creditAccountExternalId,
            BigDecimal amount,
            String currency
    ) {

        log.info("Processing ledger transaction: {}", idempotencyKey);

        try {
            LedgerTransaction transaction =
                    transactionRepository.save(
                            LedgerTransaction.create(
                                    idempotencyKey,
                                    referenceType,
                                    referenceId
                            )
                    );

            LedgerAccount debitAccount =
                    accountRepository.findByExternalAccountId(debitAccountExternalId)
                            .orElseThrow(() -> new IllegalStateException("Debit account not found"));

            LedgerAccount creditAccount =
                    accountRepository.findByExternalAccountId(creditAccountExternalId)
                            .orElseThrow(() -> new IllegalStateException("Credit account not found"));

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

            log.info("Transaction completed successfully: {}", idempotencyKey);

        } catch (DataIntegrityViolationException e) {
            // Duplicate event (idempotency hit)
            log.warn("Duplicate transaction skipped: {}", idempotencyKey);
        }
    }


    public void fallback(
            String idempotencyKey,
            String referenceType,
            String referenceId,
            Long debitAccountExternalId,
            Long creditAccountExternalId,
            BigDecimal amount,
            String currency,
            Throwable ex
    ) {
        log.error("Fallback triggered for transaction {} due to {}", idempotencyKey, ex.getMessage());

    }
}