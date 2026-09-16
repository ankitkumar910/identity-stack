package dev.ankitkumar.identitystack.security;

import dev.ankitkumar.identitystack.entity.User;
import dev.ankitkumar.identitystack.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;


    @Override
    public @NonNull CustomUserDetails loadUserByUsername(@NonNull String username) {

        log.info("Load user by username: username = {}", username);

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("User found with username: username = {}", username);
            return new CustomUserDetails(user);
        }

        throw new UsernameNotFoundException("User not found with username : " + username);
    }

    public Object loadUserByUserId(long userId) {

        log.info("Load user by id: user_id = {}", userId);

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("User found with id: user_id = {}", userId);
            return new CustomUserDetails(user);
        }

        throw new UsernameNotFoundException("User not found.");

    }
}
