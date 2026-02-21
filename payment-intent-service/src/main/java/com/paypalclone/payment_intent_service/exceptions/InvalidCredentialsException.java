package com.paypalclone.payment_intent_service.exceptions;



public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}

