package dev.ankitkumar.identitystack.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {

    public final HttpStatus status = HttpStatus.NOT_FOUND;
    public ResourceNotFoundException(String message) {

        super(message);
    }
}
