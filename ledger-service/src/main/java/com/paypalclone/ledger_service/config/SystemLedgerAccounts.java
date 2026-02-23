package com.paypalclone.ledger_service.config;


public final class SystemLedgerAccounts {

    // Money ENTERING the platform (customer / card / UPI / gateway)
    public static final Long CUSTOMER_CLEARING_ACCOUNT_EXTERNAL_ID = 888888L;

    // Money LEAVING the platform (bank payouts)
    public static final Long BANK_CLEARING_ACCOUNT_EXTERNAL_ID = 999999L;

    private SystemLedgerAccounts() {}
}