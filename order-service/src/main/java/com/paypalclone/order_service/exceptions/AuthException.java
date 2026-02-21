package com.paypalclone.order_service.exceptions;

public abstract class AuthException extends RuntimeException {
    protected AuthException(String message) {
        super(message);
    }
}