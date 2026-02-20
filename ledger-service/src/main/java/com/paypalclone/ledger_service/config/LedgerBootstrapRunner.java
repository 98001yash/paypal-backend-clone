package com.paypalclone.ledger_service.config;

import com.paypalclone.ledger_service.service.LedgerTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class LedgerBootstrapRunner implements CommandLineRunner {

    private final LedgerTransactionService ledgerTransactionService;

    @Override
    public void run(String... args) {

        ledgerTransactionService.postTransaction(
                "boot-txn-001",
                "BOOTSTRAP",
                "boot-ref",
                8L,
                10L,
                new BigDecimal("50.00"),
                "INR"
        );
    }
}
