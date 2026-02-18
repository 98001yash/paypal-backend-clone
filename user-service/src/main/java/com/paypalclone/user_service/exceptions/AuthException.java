package com.paypalclone.user_service.exceptions;

public abstract class AuthException extends RuntimeException {
    protected AuthException(String message) {
        super(message);
    }
}