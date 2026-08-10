package dev.ankitkumar.identitystack.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class ListUserResponseDto {

    private String message;
    private int code;
    private List<UserData> data;
}
