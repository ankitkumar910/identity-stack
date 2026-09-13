package dev.ankitkumar.identitystack.service.UserServiceTest;

import dev.ankitkumar.identitystack.exception.ConflictException;
import dev.ankitkumar.identitystack.exception.ParameterNotFoundException;
import dev.ankitkumar.identitystack.mapper.UserMapper;
import dev.ankitkumar.identitystack.repository.UserRepository;
import dev.ankitkumar.identitystack.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class FindAllUsersTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;


    @InjectMocks
    private UserService userService;

    @Test
    public void rejectInvalidSortProperty() {
        String sorted = "Phone";
        Assertions.assertThrows(ParameterNotFoundException.class,
                () -> userService
                        .findAllUsers("", sorted, "asc", 1, 10));
    }

    @Test
    public void rejectInvalidSortDirection(){

        String direction = "up";
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService
                        .findAllUsers("", "phone", direction, 1, 10));

    }

    @Test
    public void rejectInvalidPageNo(){

        int pageNumber = -1;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService
                        .findAllUsers("", "phone", "asc", pageNumber, 10));

    }

    @Test
    public void rejectInvalidPageSize(){

        int pageSize = 0;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService
                        .findAllUsers("", "phone", "asc",2, pageSize));

    }
}
