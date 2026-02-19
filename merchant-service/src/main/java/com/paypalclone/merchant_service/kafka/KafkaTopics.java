package com.paypalclone.merchant_service.kafka;

public final class KafkaTopics {

    public static final String MERCHANT_CREATED   = "merchant.merchant.created";
    public static final String MERCHANT_ACTIVATED = "merchant.merchant.activated";
    public static final String MERCHANT_LIMITED   = "merchant.merchant.limited";
    public static final String MERCHANT_SUSPENDED = "merchant.merchant.suspended";
    public static final String MERCHANT_REJECTED  = "merchant.merchant.rejected";

    private KafkaTopics() {}
}
