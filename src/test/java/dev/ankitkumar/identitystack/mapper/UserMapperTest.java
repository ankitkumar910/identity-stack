package dev.ankitkumar.identitystack.mapper;

import dev.ankitkumar.identitystack.dto.request.UserRegisterRequestDto;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.entity.Role;
import dev.ankitkumar.identitystack.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserMapper userMapper;

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



    @Test
    public void toUserTest() {

        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setFirstName(firstName);
        requestDto.setLastName(lastName);
        requestDto.setEmail(email);
        requestDto.setPassword(password);
        requestDto.setProfilePicture(profilePicture);
        requestDto.setUsername(username);
        requestDto.setPhone(phone);



       //define rule password encoder
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        User user = userMapper.toUser(requestDto);

        Assertions.assertEquals(firstName, user.getFirstName());
        Assertions.assertEquals(lastName,user.getLastName());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(encodedPassword,user.getPassword()); //See
        Assertions.assertEquals(profilePicture, user.getProfilePicture());
        Assertions.assertEquals(username,user.getUsername());
        Assertions.assertEquals(phone, user.getPhone());


        verify(passwordEncoder,times(1)).encode(password);


    }

    @Test
    public void toUserResponseDtoTest(){

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setPhone(phone);
        user.setUsername(username);
        user.setProfilePicture(profilePicture);
        user.setId(id);
        user.setRoles(rolesSet);
        user.setTokenVersion(tokenVersion);

        UserResponseDto responseDto = userMapper.toUserResponseDto(user,status,message);

        Assertions.assertEquals(firstName, responseDto.getData().getFirstName());
        Assertions.assertEquals(lastName, responseDto.getData().getLastName());
        Assertions.assertEquals(email, responseDto.getData().getEmail());
        Assertions.assertEquals(phone, responseDto.getData().getPhone());

        Assertions.assertEquals(profilePicture, responseDto.getData().getProfilePicture());
        Assertions.assertEquals(id, responseDto.getData().getId());
        Assertions.assertEquals(status.value(), responseDto.getCode());
        Assertions.assertEquals(message, responseDto.getMessage());

        verify(passwordEncoder,never()).encode(password);

    }




}
