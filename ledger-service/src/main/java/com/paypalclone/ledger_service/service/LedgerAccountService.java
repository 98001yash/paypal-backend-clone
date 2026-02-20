package com.paypalclone.ledger_service.service;

import com.paypalclone.ledger_service.entity.LedgerAccount;
import com.paypalclone.ledger_service.enums.LedgerAccountType;
import com.paypalclone.ledger_service.repository.LedgerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LedgerAccountService {

    private final LedgerAccountRepository repository;


    @Transactional
    public void createLedgerAccountIfNotExists(
            Long externalAccountId,
            LedgerAccountType type,
            String currency
    ) {
        repository.findByExternalAccountIdAndCurrency(externalAccountId, currency)
                .ifPresentOrElse(
                        acc -> {}, // idempotent no-op
                        () -> repository.save(
                                new LedgerAccount(
                                        externalAccountId,
                                        type,
                                        currency
                                )
                        )
                );
    }
}
