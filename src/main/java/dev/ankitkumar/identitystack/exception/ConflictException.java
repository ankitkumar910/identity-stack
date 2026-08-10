package dev.ankitkumar.identitystack.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends RuntimeException {
    public final HttpStatus status =  HttpStatus.CONFLICT;
    public ConflictException(String message) {
        super(message);
    }
}
