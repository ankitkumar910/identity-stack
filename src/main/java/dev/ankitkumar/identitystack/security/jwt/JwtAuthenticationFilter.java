package dev.ankitkumar.identitystack.security.jwt;

import dev.ankitkumar.identitystack.security.CustomUserDetails;
import dev.ankitkumar.identitystack.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authenticationToken = request.getHeader("authorization");


        if (authenticationToken == null || !authenticationToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authenticationToken.substring(7);

        if (jwtToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }


        try {

            String username = jwtService.extractUsername(jwtToken);
            System.out.println("User id : " + jwtService.extractId(jwtToken));
            long user_id = jwtService.extractId(jwtToken);



            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetailsService.loadUserByUserId(user_id), null, jwtService.extractAuthorities(jwtToken));

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (RuntimeException e) {


            String message = """
                    {
                    message : ["Access token is compromised or expired."]
                    }
                    """;
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(message);

            System.out.println("Exception in JwtFilterChain: "+e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);

    }
}
