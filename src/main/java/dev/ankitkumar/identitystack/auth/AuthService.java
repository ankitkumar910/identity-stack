package dev.ankitkumar.identitystack.auth;

import dev.ankitkumar.identitystack.exception.InvalidCredentialException;
import dev.ankitkumar.identitystack.security.CustomUserDetails;
import dev.ankitkumar.identitystack.security.jwt.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    public String login(String username, String password) {


        Authentication authRequest = new UsernamePasswordAuthenticationToken(username, password);


        try {

            Authentication authResponse = authenticationManager.authenticate(authRequest);
            CustomUserDetails customUserDetails = (CustomUserDetails) authResponse.getPrincipal();

            if(customUserDetails != null)  return jwtService.getToken(customUserDetails);

            throw  new RuntimeException("CustomUserDetails is null.");

        } catch (RuntimeException e) {
            throw new InvalidCredentialException("Invalid username or password.");
        }


    }


}
