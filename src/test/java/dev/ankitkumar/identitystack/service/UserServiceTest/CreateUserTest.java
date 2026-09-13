package dev.ankitkumar.identitystack.service.UserServiceTest;

import dev.ankitkumar.identitystack.dto.request.UserRegisterRequestDto;
import dev.ankitkumar.identitystack.entity.Role;
import dev.ankitkumar.identitystack.exception.ConflictException;
import dev.ankitkumar.identitystack.mapper.UserMapper;
import dev.ankitkumar.identitystack.repository.UserRepository;
import dev.ankitkumar.identitystack.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateUserTest {


    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    String firstName = "Ankit";
    String lastName = "Kumar";
    String email = "ankitkumar@company.com";
    String password = "Password@123#*";
    String encodedPassword = "23%^!@hhg879";
    String phone = "987654321";
    String username = "ankit2026";
    String profilePicture = "https://images.pexels.com/photos/36388575/pexels-photo-36388575.jpeg";
    long id = 100;
    Set<Role> rolesSet = Set.of(Role.USER);
    int tokenVersion = 1;
    HttpStatus status = HttpStatus.OK;
    String message = "Success.";

    UserRegisterRequestDto requestDto = new UserRegisterRequestDto();


    @BeforeEach
    public void setUp() {
        requestDto.setFirstName(firstName);
        requestDto.setLastName(lastName);
        requestDto.setEmail(email);
        requestDto.setPassword(password);
        requestDto.setProfilePicture(profilePicture);
        requestDto.setUsername(username);
        requestDto.setPhone(phone);
    }

    @Test
    public void rejectDuplicateEmailTest() {


        when(userRepository.existsByEmail(email)).thenReturn(true);

        Assertions.assertThrows(ConflictException.class, () -> {
            userService.createUser(requestDto);
        });
    }

    @Test
    public void rejectDuplicatePhone() {

        when(userRepository.existsByPhone(phone)).thenReturn(true);

        Assertions.assertThrows(ConflictException.class, () -> {
            userService.createUser(requestDto);
        });
    }

    @Test
    public void rejectWhenUserIsNull() {


        when(userMapper.toUser(requestDto)).thenReturn(null);

        Assertions.assertThrows(ConflictException.class, () -> {
            userService.createUser(requestDto);
        });
    }
}
