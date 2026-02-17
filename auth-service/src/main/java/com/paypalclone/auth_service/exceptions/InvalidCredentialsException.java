package com.paypalclone.auth_service.exceptions;

public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}

