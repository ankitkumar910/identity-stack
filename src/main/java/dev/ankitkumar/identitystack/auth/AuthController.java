package dev.ankitkumar.identitystack.auth;

import dev.ankitkumar.identitystack.dto.request.UserLoginRequestDto;
import dev.ankitkumar.identitystack.dto.response.UserLoginResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;
    @PostMapping("/login")
    public String login(@RequestBody()  @Validated UserLoginRequestDto requestDto){

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String jwtToken = authService.login(username,password);
        UserLoginResponseDto responseDto = new UserLoginResponseDto();
        responseDto.setAccessToken(jwtToken);
        return jwtToken;
    }
}
