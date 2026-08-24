package dev.ankitkumar.identitystack.security;

import dev.ankitkumar.identitystack.exception.JwtTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            if (customUserDetails != null) return customUserDetails.getUsername();
        }

        throw new JwtTokenException("Access token is compromised or expired.");
    }

    public static long getUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            if (customUserDetails != null) {

                return customUserDetails.getId();

            }
        }

        throw new JwtTokenException("Access token is compromised or expired.");
    }
}
