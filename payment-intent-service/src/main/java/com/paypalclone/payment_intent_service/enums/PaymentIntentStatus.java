package com.paypalclone.payment_intent_service.enums;

public enum PaymentIntentStatus {

    CREATED,   // intent created, nothing attempted
    AUTHORIZED,   // funds authorized / held
    CAPTURED,    // money captured
    FAILED,     //authorization/ capture failed
    CANCELLED,   // user/system cancelled
    REFUNDED    // money refunded
}
