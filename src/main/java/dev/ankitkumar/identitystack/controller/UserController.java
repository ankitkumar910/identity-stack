package dev.ankitkumar.identitystack.controller;

import dev.ankitkumar.identitystack.dto.request.UserPasswordUpdate;
import dev.ankitkumar.identitystack.dto.request.UserRegisterRequestDto;
import dev.ankitkumar.identitystack.dto.request.UserUpdateRequestDto;
import dev.ankitkumar.identitystack.dto.response.UserPasswordUpdateResponse;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.security.SecurityUtil;
import dev.ankitkumar.identitystack.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;


    @PostMapping("")
    private ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRegisterRequestDto userRequestDto) {

        UserResponseDto userResponseDto = userService.createUser(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @GetMapping("/me")
    private ResponseEntity<UserResponseDto> findMyProfile() {
        String username = SecurityUtil.getUsername();
        UserResponseDto userResponseDto = userService.findUserByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @PatchMapping("/me")
    private ResponseEntity<UserResponseDto> updateUser(@RequestBody @Valid UserUpdateRequestDto requestDto) {

        long user_id = SecurityUtil.getUserId();
        return ResponseEntity.ok(userService.updateUser(requestDto, user_id));
    }

    @PatchMapping("/me/password")
    private ResponseEntity<UserPasswordUpdateResponse> updateUser(@RequestBody @Valid UserPasswordUpdate requestDto) {

        long user_id = SecurityUtil.getUserId();
        return ResponseEntity.ok(userService.updateUserPassword(requestDto, user_id));
    }


    @DeleteMapping("/me")
    private ResponseEntity.BodyBuilder deleteUser() {


        long user_id = SecurityUtil.getUserId();

        userService.removeUserById(user_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT);

    }


}
