package dev.ankitkumar.identitystack.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserUpdateRequestDto {



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
    private String email;


    @Pattern(
            regexp = "^[a-zA-Z0-9_]{3,16}$",
            message = "Username must be 3–16 characters long and contain only letters, numbers, or underscores."
    )
    private String username;

    @Size(max = 1000, message = "Profile picture URL can't be greater than 1000 characters.")
    private String profilePicture;

}
