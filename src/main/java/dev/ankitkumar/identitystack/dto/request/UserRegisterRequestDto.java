package dev.ankitkumar.identitystack.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserRegisterRequestDto {

    @NotBlank(message = "first name is required.")
    @NotNull(message = "first name can not be null.")
    @Pattern(regexp = "^[a-zA-Z]+$",message = "first name can only contain alphabets.")
    @Size(min = 2,max = 50, message = "first name can't be grater than 50 characters and less than 2 characters.")
    private String firstName;


    @Pattern(regexp = "^[a-zA-Z]+$",message = "last name can only contain alphabets.")
    @Size(max = 50,message = "Last name can't be greater than 50 characters.")
    private String lastName;


    @Pattern(regexp = "[0-9]{10}$",
             message = "Please enter a valid phone number.")
    private String phone;

    @Email(message = "Please enter a valid email.")
    @NotNull(message = "Email is required.")
    private String email;

    @NotBlank(message = "Username is required.")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]{3,16}$",
            message = "Username must be 3–16 characters long and contain only letters, numbers, or underscores."
    )
    private String username;

    @NotBlank(message = "Password is required.")
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
    private String password;

    private String profilePicture;
}
