package dev.ankitkumar.identitystack.exception;

import dev.ankitkumar.identitystack.dto.response.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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


}
