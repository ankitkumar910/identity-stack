package dev.ankitkumar.identitystack.exception;

import dev.ankitkumar.identitystack.dto.response.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        String message = exception.getMessage();
        
        
        if(!whiteableParameters.isEmpty()){
            
            sBuilder.append(" Supported fields : ");
          //  for (String field : whiteableParameters) sBuilder.append(field).append(" ");
            sBuilder.append(whiteableParameters);
            
        }

        responseDto.getMessage().add(sBuilder.toString());


        

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDto> handleGeneralException(RuntimeException exception){
        ExceptionResponseDto responseDto = new ExceptionResponseDto();


        String message = exception.getMessage();
        System.out.println(message);

        responseDto.getMessage().add("Something went wrong. Please try after some time.");
        responseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);

    }


}
