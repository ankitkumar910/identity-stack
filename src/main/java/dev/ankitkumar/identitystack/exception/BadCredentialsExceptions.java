package dev.ankitkumar.identitystack.exception;

public class BadCredentialsExceptions extends RuntimeException {
    String message;

    public BadCredentialsExceptions(String message) {
        super(message);
        this.message = message;
    }
}
