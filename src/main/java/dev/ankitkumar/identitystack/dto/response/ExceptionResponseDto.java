package dev.ankitkumar.identitystack.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ExceptionResponseDto {

    private HttpStatus status;
    private final List<String> message = new ArrayList<>();


}
