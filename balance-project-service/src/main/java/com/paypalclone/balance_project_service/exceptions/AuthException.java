package com.paypalclone.balance_project_service.exceptions;

public abstract class AuthException extends RuntimeException {
    protected AuthException(String message) {
        super(message);
    }
}