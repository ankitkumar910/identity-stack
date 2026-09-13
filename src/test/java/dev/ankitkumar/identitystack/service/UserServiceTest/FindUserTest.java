package dev.ankitkumar.identitystack.service.UserServiceTest;

import dev.ankitkumar.identitystack.exception.BadCredentialsExceptions;
import dev.ankitkumar.identitystack.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FindUserTest {


    @InjectMocks
    private UserService userService;

    @Test
    public void rejectNegativeIdTest() {
        long id = -10;

        IllegalArgumentException exp = Assertions
                .assertThrows(IllegalArgumentException.class, () -> userService.findUserById(id));
        Assertions.assertEquals("user_id can't be negative.", exp.getMessage());
    }

    @Test
    public void rejectInvalidUsername() {

        String username = "";

        IllegalArgumentException exp = Assertions
                .assertThrows(IllegalArgumentException.class, () -> userService.findUserByUsername(username));

        Assertions.assertEquals("username is invalid.", exp.getMessage());

    }


}
