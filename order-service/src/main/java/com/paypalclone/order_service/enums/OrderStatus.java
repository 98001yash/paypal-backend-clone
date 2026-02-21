package com.paypalclone.order_service.enums;

public enum  OrderStatus {

    CREATED,  // order created, not confirmed
    CONFIRMED,  // Buyer  confirmed the order
    CANCELLED    // order cancelled before payment
}
