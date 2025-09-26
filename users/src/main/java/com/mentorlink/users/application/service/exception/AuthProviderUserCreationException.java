package com.mentorlink.users.application.service.exception;

public class AuthProviderUserCreationException extends RuntimeException {
    public AuthProviderUserCreationException(String message) {
        super(message);
    }
}
