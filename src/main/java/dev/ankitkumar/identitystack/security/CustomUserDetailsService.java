package dev.ankitkumar.identitystack.security;

import dev.ankitkumar.identitystack.entity.User;
import dev.ankitkumar.identitystack.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    UserService userService;

    @Override
    public  CustomUserDetails loadUserByUsername(String username) {



        Optional<User> userOptional = userService.findUserByUsername(username);

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return new CustomUserDetails(user);
        }

        throw  new UsernameNotFoundException("User not found with username : "+username);

    }
}
