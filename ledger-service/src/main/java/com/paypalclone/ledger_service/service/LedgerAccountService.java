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
    public void createIfNotExists(Long externalAccountId,
                                  LedgerAccountType type) {

        repository.findByExternalAccountId(externalAccountId)
                .ifPresentOrElse(
                        acc -> {}, // idempotent no-op
                        () -> repository.save(
                                LedgerAccount.create(
                                        externalAccountId,
                                        type
                                )
                        )
                );
    }

    @Transactional
    public void activate(Long externalAccountId) {
        repository.findByExternalAccountId(externalAccountId)
                .ifPresent(LedgerAccount::activate);
    }

    @Transactional
    public void block(Long externalAccountId) {
        repository.findByExternalAccountId(externalAccountId)
                .ifPresent(LedgerAccount::block);
    }


    @Transactional
    public void suspend(Long externalAccountId) {
        repository.findByExternalAccountId(externalAccountId)
                .ifPresent(LedgerAccount::block);
    }

    //  account.account.closed → BLOCK
    @Transactional
    public void close(Long externalAccountId) {
        repository.findByExternalAccountId(externalAccountId)
                .ifPresent(LedgerAccount::block);
    }
}
