package dev.ankitkumar.identitystack.mapper;

import dev.ankitkumar.identitystack.dto.request.UserRequestDto;
import dev.ankitkumar.identitystack.dto.response.ListUserResponseDto;
import dev.ankitkumar.identitystack.dto.response.UserData;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public User toUser(UserRequestDto requestDto){
        User user = new User();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setProfilePicture(requestDto.getProfilePicture());
        user.setPhone(requestDto.getPhone());

        return user;
    }

    public UserResponseDto toUserResponseDto(User user, HttpStatus statusCode, String message){

        UserData userData = getUserData(user);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setData(userData);
        userResponseDto.setMessage(message);
        userResponseDto.setCode(statusCode.value());

        return userResponseDto;
    }

    public ListUserResponseDto toListUserResponseDto(List<User> userList, String message, HttpStatus httpStatus) {
        ListUserResponseDto listUserResponseDto  = new ListUserResponseDto();

        List<UserData> userDataList = new ArrayList<>();
       for(User user : userList){
           UserData userData = getUserData(user);
           userDataList.add(userData);
       }

       listUserResponseDto.setData(userDataList);
       listUserResponseDto.setMessage(message);
       listUserResponseDto.setCode(httpStatus.value());

       return listUserResponseDto;

    }

    private UserData getUserData(User user) {

        UserData userData = new UserData();

        userData.setId(user.getId());
        userData.setEmail(user.getEmail());
        userData.setFirstName(user.getFirstName());
        userData.setLastName(user.getLastName());
        userData.setProfilePicture(user.getProfilePicture());
        userData.setPhone(user.getPhone());

        return  userData;
    }
}
