package com.paypalclone.ledger_service.repository;

import com.paypalclone.ledger_service.entity.LedgerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LedgerTransactionRepository
        extends JpaRepository<LedgerTransaction, Long> {

    Optional<LedgerTransaction> findByIdempotencyKey(String idempotencyKey);
}