package com.paypalclone.ledger_service.config;

import com.paypalclone.ledger_service.enums.LedgerAccountType;

public class LedgerAccountMapper {

    public static LedgerAccountType mapAccountType(String accountType) {
        return switch (accountType) {
            case "WALLET" -> LedgerAccountType.USER_WALLET;
            case "SETTLEMENT" -> LedgerAccountType.MERCHANT_SETTLEMENT;
            default -> throw new IllegalArgumentException(
                    "Unsupported account type: " + accountType
            );
        };
    }
}