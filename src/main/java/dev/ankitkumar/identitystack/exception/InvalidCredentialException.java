package dev.ankitkumar.identitystack.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public class InvalidCredentialException extends RuntimeException {
    private final String message;
    public InvalidCredentialException(String message) {


       super(message);
       this.message = message;
    }
}
