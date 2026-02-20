package com.paypalclone.balance_project_service.repository;

import com.paypalclone.balance_project_service.entity.BalanceProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceProjectionRepository extends JpaRepository<BalanceProjection, Long> {

    Optional<BalanceProjection> findByLedgerAccountIdAndCurrency(
            Long ledgerAccountId,
            String currency
    );
}
