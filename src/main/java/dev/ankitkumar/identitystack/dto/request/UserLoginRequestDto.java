package dev.ankitkumar.identitystack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserLoginRequestDto {

    @NotBlank(message = "Username can't be empty.")
    @NotNull(message = "Username is required.")
    private String username;

    @NotBlank(message = "Password can't be empty.")
    @NotNull(message = "Password is required.")
    private String password;
}
