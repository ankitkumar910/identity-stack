package dev.ankitkumar.identitystack.security;

import dev.ankitkumar.identitystack.entity.Role;
import dev.ankitkumar.identitystack.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomUserDetailsTest {

    @Test
    void shouldExposeUserDataAndGrantedAuthorities() {
        User user = new User();
        user.setId(99L);
        user.setUsername("ankit");
        user.setPassword("encoded-password");
        user.setRoles(Set.of(Role.USER, Role.ADMIN));
        user.setTokenVersion(5);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        assertEquals("ankit", customUserDetails.getUsername());
        assertEquals("encoded-password", customUserDetails.getPassword());
        assertEquals(99L, customUserDetails.getId());
        assertEquals(5, customUserDetails.getTokenVersion());

        Set<String> authorities = customUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertEquals(Set.of("ROLE_USER", "ROLE_ADMIN"), authorities);


    }
}
