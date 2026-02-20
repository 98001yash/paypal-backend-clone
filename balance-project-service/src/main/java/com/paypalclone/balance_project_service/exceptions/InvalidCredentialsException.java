package com.paypalclone.balance_project_service.exceptions;


public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}

