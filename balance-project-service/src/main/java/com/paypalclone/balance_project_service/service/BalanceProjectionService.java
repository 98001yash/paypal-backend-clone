package com.paypalclone.balance_project_service.service;

import com.paypalclone.balance_project_service.entity.BalanceProjection;
import com.paypalclone.balance_project_service.repository.BalanceProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class BalanceProjectionService {

    private final BalanceProjectionRepository repository;

    public void applyLedgerEntry(
            Long ledgerAccountId,
            String currency,
            String entryType,
            BigDecimal amount
    ) {

        BalanceProjection projection =
                repository.findByLedgerAccountIdAndCurrency(
                        ledgerAccountId,
                        currency
                ).orElseGet(() ->
                        createNew(ledgerAccountId, currency)
                );

        if ("CREDIT".equals(entryType)) {
            projection.credit(amount);
        } else if ("DEBIT".equals(entryType)) {
            projection.debit(amount);
        } else {
            throw new IllegalArgumentException(
                    "Unknown ledger entry type: " + entryType
            );
        }

        projection.touch();
    }

    private BalanceProjection createNew(
            Long ledgerAccountId,
            String currency
    ) {
        BalanceProjection projection = new BalanceProjection(
                ledgerAccountId,
                currency
        );
        return repository.save(projection);
    }
}
