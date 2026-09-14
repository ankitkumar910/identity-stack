package dev.ankitkumar.identitystack.security.jwt;

import dev.ankitkumar.identitystack.entity.Role;
import dev.ankitkumar.identitystack.entity.User;
import dev.ankitkumar.identitystack.exception.JwtTokenException;
import dev.ankitkumar.identitystack.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private JwtService jwtService;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "duration", 600_000L);
        ReflectionTestUtils.setField(jwtService, "issuer", "identity-stack");

        User user = new User();
        user.setId(42L);
        user.setUsername("ankit2026");
        user.setPassword("encoded-password");
        user.setRoles(Set.of(Role.USER, Role.ADMIN));
        user.setTokenVersion(7);

        userDetails = new CustomUserDetails(user);
    }

    @Test
    void getTokenShouldContainExpectedUserClaims() {
        String token = jwtService.getToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals(userDetails.getId(), jwtService.extractId(token));
        assertEquals(userDetails.getTokenVersion(), jwtService.extractTokenVersion(token));

        Set<String> authorities = jwtService.extractAuthorities(token)
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertEquals(Set.of("ROLE_USER", "ROLE_ADMIN"), authorities);
    }

    @Test
    void extractMethodsShouldThrowJwtTokenExceptionForInvalidToken() {
        assertThrows(JwtTokenException.class, () -> jwtService.extractId("invalid-token"));
        assertThrows(JwtTokenException.class, () -> jwtService.extractAuthorities("invalid-token"));
        assertThrows(JwtTokenException.class, () -> jwtService.extractTokenVersion("invalid-token"));
    }
}
