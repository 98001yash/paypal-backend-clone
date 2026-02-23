package com.paypalclone.balance_project_service.service;

import com.paypalclone.balance_project_service.entity.BalanceProjection;
import com.paypalclone.balance_project_service.repository.BalanceProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceProjectionService {

    private final BalanceProjectionRepository repository;

    @Transactional
    public void applyLedgerEntry(
            Long ledgerAccountId,
            String currency,
            String entryType,
            BigDecimal amount
    ) {

        BalanceProjection balance =
                repository.findByLedgerAccountIdAndCurrency(
                        ledgerAccountId,
                        currency
                ).orElseGet(() -> {
                    BalanceProjection bp =
                            new BalanceProjection(ledgerAccountId, currency);
                    return repository.save(bp);
                });

        if ("CREDIT".equals(entryType)) {
            balance.credit(amount);
        } else if ("DEBIT".equals(entryType)) {
            balance.debit(amount);
        } else {
            throw new IllegalArgumentException(
                    "Unknown entry type: " + entryType
            );
        }

        balance.touch();
    }
}