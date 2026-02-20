package com.paypalclone.ledger_service.config;

import com.paypalclone.ledger_service.enums.LedgerAccountType;

public final class LedgerAccountMapper {

    private LedgerAccountMapper() {}

    public static LedgerAccountType map(String accountType) {
        return LedgerAccountType.valueOf(accountType);
    }

}