package com.paypalclone.payment_intent_service.kafka;

public final class KafkaTopics {

    public static final String ORDER_CREATED   = "order.created";
    public static final String ORDER_CONFIRMED = "order.confirmed";
    public static final String ORDER_CANCELLED = "order.cancelled";

    private KafkaTopics() {}
}