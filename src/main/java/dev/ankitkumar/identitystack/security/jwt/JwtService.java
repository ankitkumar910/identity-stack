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
        System.out.println("Username " + userDetails.getUsername());


       List<String> authorityList =  userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        System.out.println("Role: " + authorityList);

        long id = userDetails.getId();
        int tokenVersion = userDetails.getTokenVersion();

      return   Jwts.builder()
                .signWith(key)
                .claim("user_id",id)
              .claim("tokenVersion",tokenVersion)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + duration))
                .issuer(issuer)
                .subject(userDetails.getUsername())
                .claim("roles",authorityList)
                .compact();
    }

    public String extractUsername(String jwtToken) {
        return claims(jwtToken).getSubject();
    }

    public long extractId(String jwtToken){

        return Long.parseLong(claims(jwtToken).get("user_id").toString());
    }

    private Claims claims(String token){

        System.out.println("Going to authenticate.");
      try{
          return Jwts.parser()
                  .verifyWith(key)
                  .build()
                  .parseSignedClaims(token)
                  .getPayload();
      } catch (JwtException e) {
          System.out.println("Found exception here.");
          System.out.println(e.getMessage());
          throw new JwtTokenException("Access token is invalid or expired.");

      }


    }

    public @Nullable List<SimpleGrantedAuthority> extractAuthorities(String jwtToken) {
        List<String> authoritiesList =  claims(jwtToken).get("roles",List.class);
        System.out.println("Authenticated! Role: "+authoritiesList);

        return authoritiesList.stream().map(SimpleGrantedAuthority::new).toList();
    }

    public int extactTokenversion(String jwtToken) {


       int tokenVersion = Integer.parseInt(claims(jwtToken).get("tokenVersion").toString());
        System.out.println("Token Version : " + tokenVersion);
        return tokenVersion;
    }
}
