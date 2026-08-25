package dev.ankitkumar.identitystack.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserPasswordUpdate {
    @NotNull(message = "Old password is required.")
    private String oldPassword;
    @NotNull(message = "New password is required.")
    @Pattern(
            regexp = ".*[a-z].*",
            message = "Password must contain at least one lowercase letter."
    )
    @Pattern(
            regexp = ".*[A-Z].*",
            message = "Password must contain at least one uppercase letter."
    )
    @Pattern(
            regexp = ".*\\d.*",
            message = "Password must contain at least one digit."
    )
    @Pattern(
            regexp = ".*[@$!%*?&].*",
            message = "Password must contain at least one special character (@, $, !, %, *, ?, or &) ."
    )
    @Size(
            min = 8,
            message = "Password must be at least 8 characters long."
    )
    private String newPassword;
}
