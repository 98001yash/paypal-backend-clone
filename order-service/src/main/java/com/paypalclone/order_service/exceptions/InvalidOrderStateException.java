package com.paypalclone.order_service.exceptions;

import com.paypalclone.order_service.enums.OrderStatus;

public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(
            Long orderId,
            OrderStatus current,
            String action
    ) {
        super("Cannot " + action +
                " order " + orderId +
                " when status is " + current);
    }
}