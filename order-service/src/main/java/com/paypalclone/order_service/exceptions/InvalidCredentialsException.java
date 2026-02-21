package com.paypalclone.order_service.exceptions;



public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}

