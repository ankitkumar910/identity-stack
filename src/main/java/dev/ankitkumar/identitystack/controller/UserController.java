package dev.ankitkumar.identitystack.controller;

import dev.ankitkumar.identitystack.dto.request.UserRegisterRequestDto;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.exception.JwtTokenException;
import dev.ankitkumar.identitystack.security.CustomUserDetails;
import dev.ankitkumar.identitystack.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;


    @PostMapping("")   //✅
    private ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRegisterRequestDto userRequestDto) {

        UserResponseDto userResponseDto = userService.createUser(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @GetMapping("/me") //✅
    private ResponseEntity<UserResponseDto> findMyProfile() {
        String username = getUsername();
        UserResponseDto userResponseDto = userService.findUserByUsername(username);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PatchMapping("/me") //✅
    private ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRegisterRequestDto requestDto) {

        long user_id = getUserId();

       ;

        return ResponseEntity.ok( userService.updateUser(requestDto,user_id));
    }

    @DeleteMapping("/me") //✅
    private ResponseEntity.BodyBuilder deleteUser() {


        long user_id = getUserId();

        userService.removeUserById(user_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT);

    }


    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
           if(customUserDetails != null) return customUserDetails.getUsername();
        }

        throw new JwtTokenException("Access token is compromised or expired.");
    }

    private long getUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println(authentication.getPrincipal());
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            if (customUserDetails != null) {
                System.out.println("Id in controller : " + customUserDetails.getId());
                return customUserDetails.getId();

            }
        }

        throw new JwtTokenException("Access token is compromised or expired.");
    }

}
