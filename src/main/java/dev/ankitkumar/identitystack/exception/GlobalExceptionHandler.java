package dev.ankitkumar.identitystack.exception;

import dev.ankitkumar.identitystack.dto.response.ExceptionResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotFoundException(ResourceNotFoundException exception) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.getMessage().add(exception.getMessage());
        responseDto.setStatus(exception.status);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponseDto> handleConflictException(ConflictException exception) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.getMessage().add(exception.getMessage());
        responseDto.setStatus(exception.status);

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

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }


    @ExceptionHandler(ParameterNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleParameterNotFoundException(ParameterNotFoundException exception) {


        StringBuilder sBuilder = new StringBuilder(exception.getMessage());
        Set<String> whiteableParameters = exception.getAllowedParameters();

        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.setStatus(HttpStatus.CONFLICT);


        if (!whiteableParameters.isEmpty()) {

            sBuilder.append(" Supported fields : ");

            sBuilder.append(whiteableParameters);

        }

        responseDto.getMessage().add(sBuilder.toString());


        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }


    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ExceptionResponseDto> handleInvalidCredentialsException(InvalidCredentialException exception) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto();


        System.out.println("ExceptionHandler\\handleInvalidCredentialsException: " + exception.getMessage());

        responseDto.getMessage().add(exception.getMessage());
        responseDto.setStatus(HttpStatus.UNAUTHORIZED);

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDto> handleGeneralException(RuntimeException exception) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        System.out.println("ExceptionHandler\\handleGeneralException: " + exception.getMessage());
        responseDto.getMessage().add("Something went wrong. Please try after some time.");
        exception.printStackTrace();
        responseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);

    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ExceptionResponseDto> handleJwtException(JwtTokenException exception) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto();


        System.out.println("ExceptionHandler\\handleJwtException: " + exception.getMessage());

        responseDto.getMessage().add(exception.getMessage());
        responseDto.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);

    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponseDto> handleAccessDeniedException(AuthorizationDeniedException exception) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto();


        System.out.println("ExceptionHandler\\handleAccessDeniedException: " + exception.getMessage());

        responseDto.getMessage().add(exception.getMessage());
        responseDto.setStatus(HttpStatus.FORBIDDEN);

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);

    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseDto> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.getMessage().add("Request body is required.");
        responseDto.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseDto);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDto> handleDataIntegrityViolation(
            DataIntegrityViolationException exception) {

        ExceptionResponseDto response = new ExceptionResponseDto();

        response.getMessage().add("Provided data exceeds the allowed limit.");
        response.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(BadCredentialsExceptions.class)
    public ResponseEntity<ExceptionResponseDto> handleBadCredentialsException(BadCredentialsExceptions badCredentialsExceptions) {

        ExceptionResponseDto response = new ExceptionResponseDto();

        response.getMessage().add("Provided old password is not valid.");
        response.setStatus(HttpStatus.BAD_REQUEST);

        System.out.println(badCredentialsExceptions.message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


}
