package dev.ankitkumar.identitystack.config;

import dev.ankitkumar.identitystack.security.jwt.JwtAuthenticationFilter;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Value("${server.port}")
    private String port;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity security, JwtAuthenticationFilter jwtAuthenticationFilter) {


        return security.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth
                        -> auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/docs","/error/**")
                        .permitAll()
                        // Public
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/users").permitAll()

                        // Authenticated
                        .requestMatchers("/api/v1/admin/users/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/**")
                        .authenticated()
                        // Everything else
                        .anyRequest().denyAll())
                .sessionManagement(customizer ->
                        customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
                            throws ServletException, IOException, java.io.IOException {
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        System.out.println("[DEBUG] Path: " + req.getRequestURI()
                                + " | Auth class: " + (auth == null ? "null" : auth.getClass().getSimpleName())
                                + " | Authorities: " + (auth == null ? "none" : auth.getAuthorities()));
                        chain.doFilter(req, res);
                    }
                }, AuthorizationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                {
                                    System.out.println("Error💻: " + authException.getMessage());
                                    authException.printStackTrace();
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                                }

                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden")

                        )


                ).build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration config) {

        return config.getAuthenticationManager();
    }

}
