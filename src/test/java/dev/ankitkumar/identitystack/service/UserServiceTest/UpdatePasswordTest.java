package dev.ankitkumar.identitystack.service.UserServiceTest;

import dev.ankitkumar.identitystack.dto.request.UserPasswordUpdate;
import dev.ankitkumar.identitystack.entity.User;
import dev.ankitkumar.identitystack.exception.BadCredentialsExceptions;
import dev.ankitkumar.identitystack.repository.UserRepository;
import dev.ankitkumar.identitystack.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdatePasswordTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    UserPasswordUpdate passwordUpdateDto = new UserPasswordUpdate();

    @BeforeEach
    public void setup() {
        String oldPassword = "Helloworld";
        String newPassword = "Kumara";

        passwordUpdateDto.setOldPassword(oldPassword);
        passwordUpdateDto.setNewPassword(newPassword);
    }

    @Test
    public void rejectNegativeIdTest() {
        long id = -10;

        IllegalArgumentException exp = Assertions
                .assertThrows(IllegalArgumentException.class, () -> userService.updateUserPassword(passwordUpdateDto, id));
        Assertions.assertEquals("user_id can't be negative.", exp.getMessage());
    }

    @Test
    public void passwordMatchTest() {
        long userId = 100;
        User user = new User();
        String oldPassword = "Helloworld";
        user.setId(userId);
        user.setPassword(oldPassword);

        when(passwordEncoder.matches(passwordUpdateDto.getOldPassword(),user.getPassword())).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        BadCredentialsExceptions bce = Assertions
                .assertThrows(BadCredentialsExceptions.class, () -> userService
                        .updateUserPassword(passwordUpdateDto, userId));

        Assertions.assertEquals("Password does not matched with older one.", bce.getMessage());
    }

    @Test
    public void rejectInvalidNewPassword(){

        passwordUpdateDto.setNewPassword("      ");
        IllegalArgumentException exp = Assertions
                .assertThrows(IllegalArgumentException.class,()->userService
                .updateUserPassword(passwordUpdateDto,12));

        Assertions.assertEquals("Provide a valid updated password.", exp.getMessage());
    }

}

