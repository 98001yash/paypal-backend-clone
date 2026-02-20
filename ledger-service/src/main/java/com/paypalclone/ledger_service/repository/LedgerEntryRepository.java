package com.paypalclone.ledger_service.repository;

import com.paypalclone.ledger_service.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerEntryRepository
        extends JpaRepository<LedgerEntry, Long> {
}
