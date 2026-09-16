package dev.ankitkumar.identitystack.security.jwt;

import dev.ankitkumar.identitystack.exception.JwtTokenException;
import dev.ankitkumar.identitystack.security.CustomUserDetails;
import dev.ankitkumar.identitystack.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {


        String authenticationToken = request.getHeader("authorization");


        if (authenticationToken == null || !authenticationToken.startsWith("Bearer ")) {
            log.warn("Access token is null or doesn't start with Bearer .");
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authenticationToken.substring(7);


        if (jwtToken.isBlank()) {
            log.warn("Jwt token is blank.");
            filterChain.doFilter(request, response);
            return;
        }


        try {


            long user_id = jwtService.extractId(jwtToken);


            int tokenVersion = jwtService.extractTokenVersion(jwtToken);

            CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUserId(user_id);

            if (customUserDetails == null)
                throw new RuntimeException("CustomUserDetails is null in JwtAuthenticationFilter.");
            int tokenVersionFromDB = customUserDetails.getTokenVersion();


            if (tokenVersion != tokenVersionFromDB)
                throw new JwtTokenException("Token version mismatch! Please re-authenticate.");

            List<SimpleGrantedAuthority> authorities = jwtService.extractAuthorities(jwtToken);

            Objects.requireNonNull(authorities, "Authorities can't be null");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("User authenticated: user_id = {}", user_id);
            }

        } catch (JwtTokenException e) {


            String message = """
                    {
                    message: ["%s"]
                    }
                    """.formatted(e.getLocalizedMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(message);
            log.warn("JWT authentication failed: {}", e.getMessage());
            return;
        } catch (RuntimeException e) {


            String message = """
                    {
                    message: ["Access token is compromised or expired."]
                    }
                    """;
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(message);

            log.error("Unexpected JWT authentication error: {}", e.getMessage(), e);


            return;
        }

        filterChain.doFilter(request, response);

    }
}
