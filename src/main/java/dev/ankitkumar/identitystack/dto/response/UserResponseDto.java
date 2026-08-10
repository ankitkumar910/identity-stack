package dev.ankitkumar.identitystack.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserResponseDto {
    private String message;
    private int code;
    private UserData data;
}
