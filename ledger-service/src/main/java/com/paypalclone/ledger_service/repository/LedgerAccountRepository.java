package com.paypalclone.ledger_service.repository;

import com.paypalclone.ledger_service.entity.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount,Long> {


    Optional<LedgerAccount> findByExternalAccountIdAndCurrency(
            Long externalAccountId,
            String currency
    );
}
