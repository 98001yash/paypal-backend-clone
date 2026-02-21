package com.paypalclone.payment_intent_service.kafka;

public final class KafkaTopics {


    public static final String ORDER_CREATED   = "order.order.created";
    public static final String ORDER_CONFIRMED = "order.order.confirmed";



    // payment intent event topics
    public static final String PAYMENT_INTENT_CREATED    = "payment.intent.created";
    public static final String PAYMENT_INTENT_AUTHORIZED = "payment.intent.authorized";
    public static final String PAYMENT_INTENT_CAPTURED   = "payment.intent.captured";
    public static final String PAYMENT_INTENT_FAILED     = "payment.intent.failed";

    private KafkaTopics() {}
}