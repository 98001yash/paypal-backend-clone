package com.paypalclone.user_service.exceptions;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException(String email) {
        super("User already exists with email: " + email);
    }
}
