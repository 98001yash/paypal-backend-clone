package com.paypalclone.order_service.kafka;

public final class KafkaTopics {

    public static final String ORDER_CREATED   = "order.order.created";
    public static final String ORDER_CONFIRMED = "order.order.confirmed";

    private KafkaTopics() {}
}