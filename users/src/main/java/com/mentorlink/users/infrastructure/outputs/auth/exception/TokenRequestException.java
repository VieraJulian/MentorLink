package com.mentorlink.users.infrastructure.outputs.auth.exception;

public class TokenRequestException extends RuntimeException {
    public TokenRequestException(String message) {
        super(message);
    }
}
