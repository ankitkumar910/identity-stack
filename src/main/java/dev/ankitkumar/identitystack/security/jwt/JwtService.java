package dev.ankitkumar.identitystack.security.jwt;

import dev.ankitkumar.identitystack.exception.JwtTokenException;
import dev.ankitkumar.identitystack.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private static final SecretKey key = Jwts.SIG.HS256.key().build();
    @Value("${jwt.token.duration}")
    private long duration;

    @Value("${jwt.token.issuer}")
    private String issuer;

    public String getToken(CustomUserDetails userDetails){

      return   Jwts.builder()
                .signWith(key)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + duration))
                .issuer(issuer)
                .subject(userDetails.getUsername())
                .claim("role","USER")
                .compact();
    }

    public String extractUsername(String jwtToken) {
        return claims(jwtToken).getSubject();
    }

    private Claims claims(String token){


      try{
          return Jwts.parser()
                  .verifyWith(key)
                  .build()
                  .parseSignedClaims(token)
                  .getPayload();
      } catch (JwtException e) {

          System.out.println(e.getMessage());
          throw new JwtTokenException("Access token is invalid or expired.");

      }


    }

    public @Nullable Collection<GrantedAuthority> extractAuthorities(String jwtToken) {
        String role = claims(jwtToken).get("role",String.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }
}
