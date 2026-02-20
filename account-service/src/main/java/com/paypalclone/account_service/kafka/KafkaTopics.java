package com.paypalclone.account_service.kafka;

public final class KafkaTopics {

    public static final String ACCOUNT_CREATED   = "account.account.created";
    public static final String ACCOUNT_ACTIVATED = "account.account.activated";
    public static final String ACCOUNT_SUSPENDED = "account.account.suspended";
    public static final String ACCOUNT_CLOSED    = "account.account.closed";

    private KafkaTopics() {}
}

