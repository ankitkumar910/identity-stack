package dev.ankitkumar.identitystack.service.UserServiceTest;

import dev.ankitkumar.identitystack.dto.request.UserPasswordUpdate;
import dev.ankitkumar.identitystack.dto.request.UserUpdateRequestDto;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.entity.User;
import dev.ankitkumar.identitystack.exception.ConflictException;
import dev.ankitkumar.identitystack.exception.ResourceNotFoundException;
import dev.ankitkumar.identitystack.mapper.UserMapper;
import dev.ankitkumar.identitystack.repository.UserRepository;
import dev.ankitkumar.identitystack.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateUserTest {


    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    public void rejectNegativeIdTest() {
        long id = -10;

        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();

        IllegalArgumentException exp = Assertions
                .assertThrows(IllegalArgumentException.class, () -> userService.updateUser(requestDto, id));
        Assertions.assertEquals("user_id can't be negative.", exp.getMessage());
    }

    @Test
    void rejectWhenUserDoesNotExist() {

        long id = 120;

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exp = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser(new UserUpdateRequestDto(), id)
        );

        Assertions.assertEquals(
                "No user found with id " + id,
                exp.getMessage()
        );
    }

    @Test
    public void shouldNotUpdateWithNullValue(){
        String prev = "Ankit";
        String newVal = null;

        Assertions.assertFalse(userService.shouldUpdateField(prev, newVal));
    }

    @Test
    public void shouldNotUpdateWithSameValue(){
        String prev = "Random";
        String newVal = "Random";
        Assertions.assertFalse(userService.shouldUpdateField(prev,newVal));
    }

    @Test
    void shouldUpdateWithDifferentValue() {
        Assertions.assertTrue(
                userService.shouldUpdateField("Ankit", "Rahul")
        );
    }

    @Test
    public void shouldNotUpdateWithBlankValue(){
        String prev = "Random";
        String newVal = "   ";
        Assertions.assertFalse(userService.shouldUpdateField(prev,newVal));
    }

    @Test
    public void rejectDuplicateEmail(){

        String oldEmail = "alien@gmail.com";
        String newEmail = "alien2@gmail.com";
        long id = 120;
        User user = new User();
        user.setEmail(oldEmail);


        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();
        requestDto.setEmail(newEmail);

        when(userRepository.existsByEmail(newEmail)).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

       ConflictException exp =  Assertions.assertThrows(ConflictException.class,() -> userService.updateUser(requestDto,id));

       Assertions.assertEquals("Email already present.",exp.getMessage());

    }

    @Test
    public void rejectDuplicatePhone(){


        String oldPhone = "9512345678";
        String newPhone = "9512345679";
        long id = 120;
        User user = new User();
        user.setPhone(oldPhone);


        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();
        requestDto.setPhone(newPhone);

        when(userRepository.existsByPhone(newPhone)).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        ConflictException exp =  Assertions.assertThrows(ConflictException.class,() -> userService.updateUser(requestDto,id));

        Assertions.assertEquals("Phone is already present.",exp.getMessage());
    }

    @Test
    public void rejectDuplicateUsername(){
        String oldUsername = "ankitkr45";
        String newUsername = "ankitkrdev";
        long id = 120;
        User user = new User();
        user.setUsername(oldUsername);


        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();
        requestDto.setUsername(newUsername);

        when(userRepository.existsByUsername(newUsername)).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        ConflictException exp =  Assertions.assertThrows(ConflictException.class,() -> userService.updateUser(requestDto,id));

        Assertions.assertEquals("Username already taken.",exp.getMessage());
    }


    @Test
    void updateEmailSuccessfully() {

        long id = 120;

        User user = new User();
        user.setEmail("old@gmail.com");

        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();
        requestDto.setEmail("new@gmail.com");
        UserResponseDto responseDto = new UserResponseDto();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        when(userRepository.existsByEmail("new@gmail.com"))
                .thenReturn(false);

        when(userRepository.save(user)).thenReturn(user);

        when(userMapper.toUserResponseDto(user, HttpStatus.OK,"User updated successfully.")).thenReturn(responseDto);

        userService.updateUser(requestDto, id);

        Assertions.assertEquals("new@gmail.com", user.getEmail());
    }

    @Test
    void shouldNotSaveWhenNothingIsUpdated() {

        long id = 120;

        User user = new User();
        user.setEmail("alien@gmail.com");

        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        userService.updateUser(requestDto, id);

        verify(userRepository, never()).save(user);
    }


}
