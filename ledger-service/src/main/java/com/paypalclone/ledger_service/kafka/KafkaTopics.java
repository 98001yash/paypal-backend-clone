package com.paypalclone.ledger_service.kafka;


public final class KafkaTopics {

    // ---------- LEDGER OUTPUT EVENTS ----------
    public static final String LEDGER_ENTRY_POSTED =
            "ledger.entry.posted";

    public static final String LEDGER_TRANSACTION_COMPLETED =
            "ledger.transaction.completed";

    private KafkaTopics() {}
}