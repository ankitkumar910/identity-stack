package dev.ankitkumar.identitystack.exception;

import dev.ankitkumar.identitystack.dto.response.ExceptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotFoundException(ResourceNotFoundException exception) {


        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.getMessage().add(exception.getMessage());
        responseDto.setStatus(exception.status);

        log.warn("ResourceNotFound: {}", exception.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponseDto> handleConflictException(ConflictException exception) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.getMessage().add(exception.getMessage());
        responseDto.setStatus(exception.status);
        log.warn("Conflict: {}", exception.getLocalizedMessage());

        return ResponseEntity.status(exception.status).body(responseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleValidationException(MethodArgumentNotValidException exception) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.setStatus(HttpStatus.BAD_REQUEST);


        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {

            String errorMessage = fieldError.getDefaultMessage();
            responseDto.getMessage().add(errorMessage);

        }

        log.warn("Validation failed.");

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }


    @ExceptionHandler(ParameterNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleParameterNotFoundException(ParameterNotFoundException exception) {


        StringBuilder sBuilder = new StringBuilder(exception.getMessage());
        Set<String> allowedParameters = exception.getAllowedParameters();

        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.setStatus(HttpStatus.CONFLICT);


        if (!allowedParameters.isEmpty()) {

            sBuilder.append(" Supported fields : ");

            sBuilder.append(allowedParameters);

        }

        responseDto.getMessage().add(sBuilder.toString());

        log.warn("ParameterNotFound: {}", exception.getMessage());


        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }


    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ExceptionResponseDto> handleInvalidCredentialsException(InvalidCredentialException exception) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto();


        responseDto.getMessage().add(exception.getMessage());
        responseDto.setStatus(HttpStatus.UNAUTHORIZED);

        log.warn("InvalidCredential: {}", exception.getMessage());

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDto> handleGeneralException(RuntimeException exception) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.getMessage().add("Something went wrong. Please try after some time.");
        responseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Unexpected application error", exception);
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);

    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ExceptionResponseDto> handleJwtException(JwtTokenException exception) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto();


        responseDto.getMessage().add(exception.getMessage());
        responseDto.setStatus(HttpStatus.BAD_REQUEST);
        log.warn("Jwt token exception: {}", exception.getMessage());

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);

    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponseDto> handleAccessDeniedException(AuthorizationDeniedException exception) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto();


        responseDto.getMessage().add(exception.getMessage());
        responseDto.setStatus(HttpStatus.FORBIDDEN);
        log.warn("Authorization denied: {}", exception.getMessage());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);

    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseDto> handleHttpMessageNotReadable() {

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.getMessage().add("Request body is required.");
        responseDto.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseDto);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDto> handleDataIntegrityViolation() {

        ExceptionResponseDto response = new ExceptionResponseDto();

        response.getMessage().add("Provided data exceeds the allowed limit.");
        response.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(BadCredentialsExceptions.class)
    public ResponseEntity<ExceptionResponseDto> handleBadCredentialsException() {

        ExceptionResponseDto response = new ExceptionResponseDto();

        response.getMessage().add("Provided old password is not valid.");
        response.setStatus(HttpStatus.BAD_REQUEST);


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto> handleIllegalArgsException(IllegalArgumentException e) {

        ExceptionResponseDto response = new ExceptionResponseDto();

        response.getMessage().add(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST);

        log.warn("Illegal argument: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        ExceptionResponseDto response = new ExceptionResponseDto();

        response.getMessage().add("Invalid request received.");
        response.setStatus(HttpStatus.BAD_REQUEST);
        log.warn("Method args mismatch: {}", e.getLocalizedMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


}
