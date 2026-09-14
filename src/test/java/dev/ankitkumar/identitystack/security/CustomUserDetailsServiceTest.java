package dev.ankitkumar.identitystack.security;

import dev.ankitkumar.identitystack.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public  void loadUserByUsernameShouldThrowExceptionTest(){

        String username = "ankit";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

      UsernameNotFoundException exception =   Assertions.assertThrows(UsernameNotFoundException.class,() -> customUserDetailsService.loadUserByUsername(username));

      Assertions.assertEquals("User not found with username : "+username,exception.getMessage());


    }


    @Test
    public void loadUserByUserIdShouldThrowExceptionTest(){

        long id = 100L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        UsernameNotFoundException exception =   Assertions.assertThrows(UsernameNotFoundException.class,() -> customUserDetailsService.loadUserByUserId(id));

        Assertions.assertEquals("User not found.",exception.getMessage());

    }
}
