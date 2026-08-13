package dev.ankitkumar.identitystack.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserRequestDto {

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

    private String profilePicture;
}
