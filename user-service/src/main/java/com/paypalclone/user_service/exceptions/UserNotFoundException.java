package com.paypalclone.user_service.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super("User not found for userId=" + userId);
    }

}