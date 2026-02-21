package com.paypalclone.payment_intent_service.exceptions;

public abstract class AuthException extends RuntimeException {
    protected AuthException(String message) {
        super(message);
    }
}