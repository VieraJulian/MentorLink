package com.mentorlink.users.infrastructure.outputs.auth.exception;

public class KeycloakUserCreationException extends RuntimeException {
    public KeycloakUserCreationException(String message) {
      super(message);
    }
}
