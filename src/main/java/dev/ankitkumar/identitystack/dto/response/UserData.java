package dev.ankitkumar.identitystack.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@Setter
@Getter
public class UserData {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String profilePicture;

}
