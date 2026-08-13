package dev.ankitkumar.identitystack.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ParameterNotFoundException extends RuntimeException {
    private String message;
    private Set<String> allowedParameters;

    public ParameterNotFoundException(String message, Set<String> allowedParameters) {
        super(message);
        this.message = message;
        this.allowedParameters = allowedParameters;
    }
}
